package org.datasource.jdbc.views.governance;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela PostgreSQL: public.company_ai_governance_pg
*/
@Service
public class CompanyAiGovernancePgViewBuilder {

    private static final Logger logger = Logger.getLogger(CompanyAiGovernancePgViewBuilder.class.getName());

    // Numărul implicit de rânduri returnate dacă endpointul este apelat fără ?limit=
    private static final int DEFAULT_LIMIT = 1000;

    // Limită maximă ca să evităm blocarea browserului / IntelliJ / SparkSQL
    private static final int MAX_LIMIT = 7000;

    /*
        SQL Map:
        Selectăm coloanele din tabela PostgreSQL.

        ORDER BY MD5(...) produce o selecție pseudo-random deterministă:
        - nu ia doar primele rânduri din tabel
        - păstrează aceeași ordine la fiecare rulare
        - este util pentru testare și integrare stabilă
    */
    private String SQL_COMPANY_AI_GOVERNANCE_PG_SELECT =
            "SELECT response_id, company_id, survey_year, quarter, " +
                    "ai_training_hours, ai_maturity_score, ai_failure_rate, ai_risk_management_score " +
                    "FROM public.company_ai_governance_pg " +
                    "ORDER BY MD5(CAST(response_id AS TEXT) || 'IIS_SAMPLE_2026') " +
                    "LIMIT ?";

    // DataCache: lista returnată către controller
    private List<CompanyAiGovernancePgView> companyAiGovernancePgViewList = new ArrayList<>();

    public List<CompanyAiGovernancePgView> getViewList() {
        return this.companyAiGovernancePgViewList;
    }

    /*
        Metoda standard, compatibilă cu stilul profesorului.
        Dacă este apelată simplu, returnează DEFAULT_LIMIT rânduri.
    */
    public CompanyAiGovernancePgViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Va fi folosită de RESTViewServiceJDBC când endpointul primește ?limit=...
    */
    public CompanyAiGovernancePgViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompanyAiGovernancePgView with limit = " + safeLimit);

        try (Connection jdbcConnection = jdbcConnector.getConnection();
             PreparedStatement selectStmt = jdbcConnection.prepareStatement(SQL_COMPANY_AI_GOVERNANCE_PG_SELECT)) {

            // Setăm valoarea pentru LIMIT ?
            selectStmt.setInt(1, safeLimit);

            ResultSet rs = selectStmt.executeQuery();

            // Reinițializăm lista la fiecare build
            companyAiGovernancePgViewList = new ArrayList<>();

            // Mapăm fiecare rând SQL într-un obiect Java View
            while (rs.next()) {
                Integer response_id = rs.getInt("response_id");
                String company_id = rs.getString("company_id");
                Integer survey_year = rs.getInt("survey_year");
                String quarter = rs.getString("quarter");

                Integer ai_training_hours = rs.getInt("ai_training_hours");
                Double ai_maturity_score = rs.getDouble("ai_maturity_score");
                Double ai_failure_rate = rs.getDouble("ai_failure_rate");
                Double ai_risk_management_score = rs.getDouble("ai_risk_management_score");

                this.companyAiGovernancePgViewList.add(
                        new CompanyAiGovernancePgView(
                                response_id,
                                company_id,
                                survey_year,
                                quarter,
                                ai_training_hours,
                                ai_maturity_score,
                                ai_failure_rate,
                                ai_risk_management_score
                        )
                );
            }

        } catch (Exception ex) {
            logger.severe("Error while building CompanyAiGovernancePgView: " + ex.getMessage());
            ex.printStackTrace();
        }

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

    /* JDBC Session Management ---------------------------------------- */

    private JDBCDataSourceConnector jdbcConnector;

    public CompanyAiGovernancePgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}