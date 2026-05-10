package org.datasource.jdbc.views.governance;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompanyAiGovernancePgViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyAiGovernancePgViewBuilder.class.getName());

    // SQL Map
    private String SQL_COMPANY_AI_GOVERNANCE_PG_SELECT = """
            SELECT
                response_id,
                company_id,
                survey_year,
                quarter,
                ai_training_hours,
                ai_maturity_score,
                ai_failure_rate,
                ai_risk_management_score
            FROM public.company_ai_governance_pg
            """;

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    // DataCache
    private List<CompanyAiGovernancePgView> companyAiGovernancePgViewList = new ArrayList<>();

    public List<CompanyAiGovernancePgView> getViewList() {
        return this.companyAiGovernancePgViewList;
    }

    // building steps
    public CompanyAiGovernancePgViewBuilder build() {
        logger.info(">>> Building CompanyAiGovernancePgView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            String sql = SQL_COMPANY_AI_GOVERNANCE_PG_SELECT;
            PreparedStatement selectStmt;

            // Prepare fetch SQL
            if (fetchOffset != null && fetchOffset > 0) {
                sql = String.format(SQL_FETCH_SELECT, SQL_COMPANY_AI_GOVERNANCE_PG_SELECT);
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
            companyAiGovernancePgViewList = new ArrayList<>();

            while (rs.next()) {
                Integer responseId = rs.getInt("response_id");
                String companyId = rs.getString("company_id");
                Integer surveyYear = rs.getInt("survey_year");
                String quarter = rs.getString("quarter");
                Integer aiTrainingHours = rs.getInt("ai_training_hours");
                BigDecimal aiMaturityScore = rs.getBigDecimal("ai_maturity_score");
                BigDecimal aiFailureRate = rs.getBigDecimal("ai_failure_rate");
                BigDecimal aiRiskManagementScore = rs.getBigDecimal("ai_risk_management_score");

                this.companyAiGovernancePgViewList.add(
                        new CompanyAiGovernancePgView(
                                responseId,
                                companyId,
                                surveyYear,
                                quarter,
                                aiTrainingHours,
                                aiMaturityScore,
                                aiFailureRate,
                                aiRiskManagementScore
                        )
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    /* JDBC Session Management ---------------------------------------- */
    private JDBCDataSourceConnector jdbcConnector;

    public CompanyAiGovernancePgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    public CompanyAiGovernancePgViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompanyAiGovernancePgViewBuilder setFetchSize(Integer fetchSize) {
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