package org.datasource.jdbc.views.adoption;

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
public class CompanyAiAdoptionPgViewBuilder {
    private static Logger logger = Logger.getLogger(CompanyAiAdoptionPgViewBuilder.class.getName());

    // SQL Map
    private String SQL_COMPANY_AI_ADOPTION_PG_SELECT = """
            SELECT
                response_id,
                company_id,
                survey_year,
                quarter,
                ai_adoption_rate,
                ai_adoption_stage,
                years_using_ai,
                ai_primary_tool,
                num_ai_tools_used,
                ai_use_case,
                ai_projects_active
            FROM public.company_ai_adoption_pg
            """;

    private Integer fetchOffset = -1;
    private Integer fetchSize = 25;

    // DataCache
    private List<CompanyAiAdoptionPgView> companyAiAdoptionPgViewList = new ArrayList<>();

    public List<CompanyAiAdoptionPgView> getViewList() {
        return this.companyAiAdoptionPgViewList;
    }

    // building steps
    public CompanyAiAdoptionPgViewBuilder build() {
        logger.info(">>> Building CompanyAiAdoptionPgView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);

        try (Connection jdbcConnection = jdbcConnector.getConnection()) {
            String sql = SQL_COMPANY_AI_ADOPTION_PG_SELECT;
            PreparedStatement selectStmt;

            // Prepare fetch SQL
            if (fetchOffset != null && fetchOffset > 0) {
                sql = String.format(SQL_FETCH_SELECT, SQL_COMPANY_AI_ADOPTION_PG_SELECT);
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
            companyAiAdoptionPgViewList = new ArrayList<>();

            while (rs.next()) {
                Integer responseId = rs.getInt("response_id");
                String companyId = rs.getString("company_id");
                Integer surveyYear = rs.getInt("survey_year");
                String quarter = rs.getString("quarter");
                BigDecimal aiAdoptionRate = rs.getBigDecimal("ai_adoption_rate");
                String aiAdoptionStage = rs.getString("ai_adoption_stage");
                Integer yearsUsingAi = rs.getInt("years_using_ai");
                String aiPrimaryTool = rs.getString("ai_primary_tool");
                Integer numAiToolsUsed = rs.getInt("num_ai_tools_used");
                String aiUseCase = rs.getString("ai_use_case");
                Integer aiProjectsActive = rs.getInt("ai_projects_active");

                this.companyAiAdoptionPgViewList.add(
                        new CompanyAiAdoptionPgView(
                                responseId,
                                companyId,
                                surveyYear,
                                quarter,
                                aiAdoptionRate,
                                aiAdoptionStage,
                                yearsUsingAi,
                                aiPrimaryTool,
                                numAiToolsUsed,
                                aiUseCase,
                                aiProjectsActive
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

    public CompanyAiAdoptionPgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    public CompanyAiAdoptionPgViewBuilder setFetchOffset(Integer fetchOffset) {
        if (fetchOffset != null) {
            this.fetchOffset = fetchOffset;
        }
        return this;
    }

    public CompanyAiAdoptionPgViewBuilder setFetchSize(Integer fetchSize) {
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