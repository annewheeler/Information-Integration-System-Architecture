package org.datasource.jpa.views.businessoutcomes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompanyBusinessOutcomesOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyBusinessOutcomesOrclViewBuilder.class.getName());

    protected String JPQL_COMPANY_BUSINESS_OUTCOMES_ORCL_SELECT =
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
                    + "ORDER BY b.responseId";

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    protected List<CompanyBusinessOutcomesOrclView> companyBusinessOutcomesOrclViewList = new ArrayList<>();

    public List<CompanyBusinessOutcomesOrclView> getCompanyBusinessOutcomesOrclViewList() {
        return companyBusinessOutcomesOrclViewList;
    }

    public CompanyBusinessOutcomesOrclViewBuilder build() {
        return this.select();
    }

    protected CompanyBusinessOutcomesOrclViewBuilder select() {
        EntityManager em = dataSourceConnector.getEntityManager();

        logger.info(">>> Building CompanyBusinessOutcomesOrclView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        Query viewQuery = em.createQuery(JPQL_COMPANY_BUSINESS_OUTCOMES_ORCL_SELECT);

        if (fetchOffset != null && fetchOffset > 0) {
            viewQuery.setFirstResult(fetchOffset - 1);
            viewQuery.setMaxResults(fetchSize);
        }

        this.companyBusinessOutcomesOrclViewList = viewQuery.getResultList();

        return this;
    }

    protected JPADataSourceConnector dataSourceConnector;

    public CompanyBusinessOutcomesOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public CompanyBusinessOutcomesOrclViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompanyBusinessOutcomesOrclViewBuilder setFetchSize(Integer fetchSize) {
        if (fetchSize != null) {
            this.fetchSize = fetchSize;
        }
        return this;
    }
}