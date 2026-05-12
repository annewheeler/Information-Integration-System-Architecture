package org.datasource.jpa.views.businessoutcomes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela Oracle: AI_DATA.COMPANY_BUSINESS_OUTCOMES
*/
@Service
public class CompanyBusinessOutcomesOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyBusinessOutcomesOrclViewBuilder.class.getName());

    // Numărul implicit de rânduri dacă endpointul este apelat fără ?limit=
    private static final int DEFAULT_LIMIT = 1000;

    // Limită maximă pentru a evita blocarea browserului / IntelliJ / SparkSQL
    private static final int MAX_LIMIT = 7000;

    /*
        JPQL Map:
        SELECT NEW construiește direct obiecte CompanyBusinessOutcomesOrclView.

        ORDER BY function('ORA_HASH', ...)
        - produce o ordine pseudo-random deterministă în Oracle
        - nu ia mereu doar primele rânduri din tabel
        - păstrează aceeași selecție la fiecare rulare
    */
    protected String JPQL_COMPANY_BUSINESS_OUTCOMES_SELECT =
            "SELECT NEW org.datasource.jpa.views.businessoutcomes.CompanyBusinessOutcomesOrclView("
                    + "b.responseId, "
                    + "b.companyId, "
                    + "b.surveyYear, "
                    + "b.quarter, "
                    + "b.productivityChangePercent, "
                    + "b.revenueGrowthPercent, "
                    + "b.costReductionPercent, "
                    + "b.innovationScore, "
                    + "b.customerSatisfaction) "
                    + "FROM CompanyBusinessOutcomesOrclView b "
                    + "ORDER BY function('ORA_HASH', CONCAT(STR(b.responseId), 'IIS_SAMPLE_2026'))";

    // DataCache: lista returnată către controller
    protected List<CompanyBusinessOutcomesOrclView> companyBusinessOutcomesOrclViewList = new ArrayList<>();

    public List<CompanyBusinessOutcomesOrclView> getCompanyBusinessOutcomesOrclViewList() {
        return companyBusinessOutcomesOrclViewList;
    }

    /*
        Metoda standard, compatibilă cu stilul profesorului.
        Dacă este apelată simplu, returnează DEFAULT_LIMIT rânduri.
    */
    public CompanyBusinessOutcomesOrclViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Va fi folosită de RESTViewServiceJPA când endpointul primește ?limit=...
    */
    public CompanyBusinessOutcomesOrclViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompanyBusinessOutcomesOrclView with limit = " + safeLimit);

        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createQuery(JPQL_COMPANY_BUSINESS_OUTCOMES_SELECT);

        // Limitarea în JPA/Oracle se face cu setMaxResults, nu cu LIMIT în SQL
        viewQuery.setMaxResults(safeLimit);

        this.companyBusinessOutcomesOrclViewList = viewQuery.getResultList();

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

    public CompanyBusinessOutcomesOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}