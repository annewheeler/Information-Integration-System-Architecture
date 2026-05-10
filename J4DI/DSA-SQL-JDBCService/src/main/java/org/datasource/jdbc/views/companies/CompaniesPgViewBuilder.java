package org.datasource.jdbc.views.companies;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompaniesPgViewBuilder {
    private static Logger logger = Logger.getLogger(CompaniesPgViewBuilder.class.getName());

    // SQL Map
    private String SQL_COMPANIES_PG_SELECT =
            "SELECT company_id, industry, country, company_size FROM public.companies_pg";

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    // DataCache
    private List<CompaniesPgView> companiesPgViewList = new ArrayList<>();

    public List<CompaniesPgView> getViewList() {
        return this.companiesPgViewList;
    }

    // building steps
    public CompaniesPgViewBuilder build() {
        logger.info(">>> Building CompaniesPgView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            String sql = SQL_COMPANIES_PG_SELECT;
            PreparedStatement selectStmt;

            // Prepare fetch SQL
            if (fetchOffset != null && fetchOffset > 0) {
                sql = String.format(SQL_FETCH_SELECT, SQL_COMPANIES_PG_SELECT);
                logger.info(">>> SQL_FETCH_SELECT formatted:\n" + sql);

                selectStmt = jdbcConnection.prepareStatement(sql);
                selectStmt.setInt(1, fetchOffset);
                selectStmt.setInt(2, fetchOffset + fetchSize);
            } else {
                selectStmt = jdbcConnection.prepareStatement(sql);
            }

            // extract data
            ResultSet rs = selectStmt.executeQuery();

            // map data to EntityView
            companiesPgViewList = new ArrayList<>();

            while (rs.next()) {
                String companyId = rs.getString("company_id");
                String industry = rs.getString("industry");
                String country = rs.getString("country");
                String companySize = rs.getString("company_size");

                this.companiesPgViewList.add(
                        new CompaniesPgView(companyId, industry, country, companySize)
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    /* JDBC Session Management ---------------------------------------- */
    private JDBCDataSourceConnector jdbcConnector;

    public CompaniesPgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    public CompaniesPgViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompaniesPgViewBuilder setFetchSize(Integer fetchSize) {
        if (fetchSize != null) {
            this.fetchSize = fetchSize;
        }
        return this;
    }

    private String SQL_FETCH_SELECT = """
            SELECT *
              FROM (
                   SELECT Q_.*,
                          ROW_NUMBER() OVER(
                                  ORDER BY 1
                          ) RN___
                     FROM (
                          %s
                   ) Q_
            ) Q__
             WHERE RN___ BETWEEN ? AND ?
            """;
}