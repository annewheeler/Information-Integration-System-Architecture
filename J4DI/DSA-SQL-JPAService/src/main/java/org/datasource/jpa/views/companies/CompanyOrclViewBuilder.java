package org.datasource.jpa.views.companies;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompanyOrclViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyOrclViewBuilder.class.getName());

    protected String JPQL_COMPANIES_ORCL_SELECT =
            "SELECT NEW org.datasource.jpa.views.companies.CompanyOrclView("
                    + "c.companyId, "
                    + "c.country, "
                    + "c.companySize, "
                    + "c.numEmployees, "
                    + "c.annualRevenueUsdMillions) "
                    + "FROM CompanyOrclView c "
                    + "ORDER BY c.companyId";

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    protected List<CompanyOrclView> companyOrclViewList = new ArrayList<>();

    public List<CompanyOrclView> getCompanyOrclViewList() {
        return companyOrclViewList;
    }

    public CompanyOrclViewBuilder build() {
        return this.select();
    }

    protected CompanyOrclViewBuilder select() {
        EntityManager em = dataSourceConnector.getEntityManager();

        logger.info(">>> Building CompanyOrclView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        Query viewQuery = em.createQuery(JPQL_COMPANIES_ORCL_SELECT);

        if (fetchOffset != null && fetchOffset > 0) {
            viewQuery.setFirstResult(fetchOffset - 1);
            viewQuery.setMaxResults(fetchSize);
        }

        this.companyOrclViewList = viewQuery.getResultList();

        return this;
    }

    protected JPADataSourceConnector dataSourceConnector;

    public CompanyOrclViewBuilder(JPADataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public CompanyOrclViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompanyOrclViewBuilder setFetchSize(Integer fetchSize) {
        if (fetchSize != null) {
            this.fetchSize = fetchSize;
        }
        return this;
    }
}