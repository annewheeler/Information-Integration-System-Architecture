package org.datasource.jpa.views.workforce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela Oracle: AI_DATA.COMPANY_WORKFORCE_IMPACT
*/
@Service
public class CompanyWorkforceImpactOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyWorkforceImpactOrclViewBuilder.class.getName());

    // Numărul implicit de rânduri dacă endpointul este apelat fără ?limit=
    private static final int DEFAULT_LIMIT = 1000;

    // Limită maximă pentru a evita blocarea browserului / IntelliJ / SparkSQL
    private static final int MAX_LIMIT = 7000;

    /*
        JPQL Map:
        SELECT NEW construiește direct obiecte CompanyWorkforceImpactOrclView.

        ORDER BY function('ORA_HASH', ...)
        - produce o ordine pseudo-random deterministă în Oracle
        - nu ia mereu doar primele rânduri din tabel
        - păstrează aceeași selecție la fiecare rulare
    */
    protected String JPQL_COMPANY_WORKFORCE_IMPACT_SELECT =
            "SELECT NEW org.datasource.jpa.views.workforce.CompanyWorkforceImpactOrclView("
                    + "w.responseId, "
                    + "w.companyId, "
                    + "w.surveyYear, "
                    + "w.quarter, "
                    + "w.remoteWorkPercentage, "
                    + "w.employeeSatisfactionScore, "
                    + "w.taskAutomationRate, "
                    + "w.timeSavedPerWeek, "
                    + "w.jobsDisplaced, "
                    + "w.jobsCreated, "
                    + "w.reskilledEmployees) "
                    + "FROM CompanyWorkforceImpactOrclView w "
                    + "ORDER BY function('ORA_HASH', CONCAT(STR(w.responseId), 'IIS_SAMPLE_2026'))";

    // DataCache: lista returnată către controller
    protected List<CompanyWorkforceImpactOrclView> companyWorkforceImpactOrclViewList = new ArrayList<>();

    public List<CompanyWorkforceImpactOrclView> getCompanyWorkforceImpactOrclViewList() {
        return companyWorkforceImpactOrclViewList;
    }

    /*
        Metoda standard, compatibilă cu stilul profesorului.
        Dacă este apelată simplu, returnează DEFAULT_LIMIT rânduri.
    */
    public CompanyWorkforceImpactOrclViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Va fi folosită de RESTViewServiceJPA când endpointul primește ?limit=...
    */
    public CompanyWorkforceImpactOrclViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompanyWorkforceImpactOrclView with limit = " + safeLimit);

        EntityManager em = dataSourceConnector.getEntityManager();
        Query viewQuery = em.createQuery(JPQL_COMPANY_WORKFORCE_IMPACT_SELECT);

        // Limitarea în JPA/Oracle se face cu setMaxResults, nu cu LIMIT în SQL
        viewQuery.setMaxResults(safeLimit);

        this.companyWorkforceImpactOrclViewList = viewQuery.getResultList();

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

    public CompanyWorkforceImpactOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }
}