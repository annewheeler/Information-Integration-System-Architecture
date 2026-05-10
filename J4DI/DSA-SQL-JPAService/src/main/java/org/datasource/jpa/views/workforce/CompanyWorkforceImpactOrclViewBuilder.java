package org.datasource.jpa.views.workforce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompanyWorkforceImpactOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyWorkforceImpactOrclViewBuilder.class.getName());

    protected String JPQL_COMPANY_WORKFORCE_IMPACT_ORCL_SELECT =
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
                    + "ORDER BY w.responseId";

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    protected List<CompanyWorkforceImpactOrclView> companyWorkforceImpactOrclViewList = new ArrayList<>();

    public List<CompanyWorkforceImpactOrclView> getCompanyWorkforceImpactOrclViewList() {
        return companyWorkforceImpactOrclViewList;
    }

    public CompanyWorkforceImpactOrclViewBuilder build() {
        return this.select();
    }

    protected CompanyWorkforceImpactOrclViewBuilder select() {
        EntityManager em = dataSourceConnector.getEntityManager();

        logger.info(">>> Building CompanyWorkforceImpactOrclView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        Query viewQuery = em.createQuery(JPQL_COMPANY_WORKFORCE_IMPACT_ORCL_SELECT);

        if (fetchOffset != null && fetchOffset > 0) {
            viewQuery.setFirstResult(fetchOffset - 1);
            viewQuery.setMaxResults(fetchSize);
        }

        this.companyWorkforceImpactOrclViewList = viewQuery.getResultList();

        return this;
    }

    protected JPADataSourceConnector dataSourceConnector;

    public CompanyWorkforceImpactOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public CompanyWorkforceImpactOrclViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompanyWorkforceImpactOrclViewBuilder setFetchSize(Integer fetchSize) {
        if (fetchSize != null) {
            this.fetchSize = fetchSize;
        }
        return this;
    }
}