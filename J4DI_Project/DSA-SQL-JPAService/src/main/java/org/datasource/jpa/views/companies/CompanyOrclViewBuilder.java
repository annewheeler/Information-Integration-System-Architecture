package org.datasource.jpa.views.companies;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela Oracle: AI_DATA.COMPANIES
*/
@Service
public class CompanyOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyOrclViewBuilder.class.getName());

    // Numărul implicit de rânduri dacă endpointul este apelat fără ?limit=
    private static final int DEFAULT_LIMIT = 1000;

    // Limită maximă pentru a evita blocarea browserului / IntelliJ / SparkSQL
    private static final int MAX_LIMIT = 7000;

    /*
        JPQL Map:
        SELECT NEW construiește direct obiecte CompanyOrclView.

        ORDER BY function('ORA_HASH', ...)
        - produce o ordine pseudo-random deterministă în Oracle
        - nu ia mereu doar primele companii din tabel
        - păstrează aceeași selecție la fiecare rulare
    */
    protected String JPQL_COMPANIES_SELECT =
            "SELECT NEW org.datasource.jpa.views.companies.CompanyOrclView("
                    + "c.companyId, "
                    + "c.country, "
                    + "c.companySize, "
                    + "c.numEmployees, "
                    + "c.annualRevenueUsdMillions) "
                    + "FROM CompanyOrclView c "
                    + "ORDER BY function('ORA_HASH', CONCAT(c.companyId, 'IIS_SAMPLE_2026'))";

    // DataCache: lista returnată către controller
    protected List<CompanyOrclView> companyOrclViewList = new ArrayList<>();

    public List<CompanyOrclView> getCompanyOrclViewList() {
        return companyOrclViewList;
    }

    /*
        Metoda standard, compatibilă cu stilul profesorului.
        Dacă este apelată simplu, returnează DEFAULT_LIMIT rânduri.
    */
    public CompanyOrclViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Va fi folosită de RESTViewServiceJPA când endpointul primește ?limit=...
    */
    public CompanyOrclViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompanyOrclView with limit = " + safeLimit);

        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createQuery(JPQL_COMPANIES_SELECT);

        // Limitarea în JPA/Oracle se face cu setMaxResults, nu cu LIMIT în SQL
        viewQuery.setMaxResults(safeLimit);

        this.companyOrclViewList = viewQuery.getResultList();

        return this;
    }

    /*
        Normalizează limita cerută:
        - null sau <= 0 devine 1000
        - peste 7000 este redus automat la 7000
    */
    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    // JPA Session Management ----------------------------------------

    protected JPADataSourceConnector dataSourceConnector;

    public CompanyOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}