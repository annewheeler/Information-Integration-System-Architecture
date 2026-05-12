package org.datasource.jdbc.views.adoption;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela PostgreSQL: public.company_ai_adoption_pg

*/
@Service
public class CompanyAiAdoptionPgViewBuilder {

    private static final Logger logger = Logger.getLogger(CompanyAiAdoptionPgViewBuilder.class.getName());

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
    private String SQL_COMPANY_AI_ADOPTION_PG_SELECT =
            "SELECT response_id, company_id, survey_year, quarter, " +
                    "ai_adoption_rate, ai_adoption_stage, years_using_ai, " +
                    "ai_primary_tool, num_ai_tools_used, ai_use_case, ai_projects_active " +
                    "FROM public.company_ai_adoption_pg " +
                    "ORDER BY MD5(CAST(response_id AS TEXT) || 'IIS_SAMPLE_2026') " +
                    "LIMIT ?";

    // DataCache: lista returnată către controller
    private List<CompanyAiAdoptionPgView> companyAiAdoptionPgViewList = new ArrayList<>();

    public List<CompanyAiAdoptionPgView> getViewList() {
        return this.companyAiAdoptionPgViewList;
    }

    /*
        Metoda standard, compatibilă cu stilul profesorului.
        Dacă este apelată simplu, returnează DEFAULT_LIMIT rânduri.
    */
    public CompanyAiAdoptionPgViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Va fi folosită de RESTViewServiceJDBC când endpointul primește ?limit=...
    */
    public CompanyAiAdoptionPgViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompanyAiAdoptionPgView with limit = " + safeLimit);

        try (Connection jdbcConnection = jdbcConnector.getConnection();
             PreparedStatement selectStmt = jdbcConnection.prepareStatement(SQL_COMPANY_AI_ADOPTION_PG_SELECT)) {

            // Setăm valoarea pentru LIMIT ?
            selectStmt.setInt(1, safeLimit);

            ResultSet rs = selectStmt.executeQuery();

            // Reinițializăm lista la fiecare build
            companyAiAdoptionPgViewList = new ArrayList<>();

            // Mapăm fiecare rând SQL într-un obiect Java View
            while (rs.next()) {
                Integer response_id = rs.getInt("response_id");
                String company_id = rs.getString("company_id");
                Integer survey_year = rs.getInt("survey_year");
                String quarter = rs.getString("quarter");

                Double ai_adoption_rate = rs.getDouble("ai_adoption_rate");
                String ai_adoption_stage = rs.getString("ai_adoption_stage");
                Integer years_using_ai = rs.getInt("years_using_ai");
                String ai_primary_tool = rs.getString("ai_primary_tool");
                Integer num_ai_tools_used = rs.getInt("num_ai_tools_used");
                String ai_use_case = rs.getString("ai_use_case");
                Integer ai_projects_active = rs.getInt("ai_projects_active");

                this.companyAiAdoptionPgViewList.add(
                        new CompanyAiAdoptionPgView(
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
                        )
                );
            }

        } catch (Exception ex) {
            logger.severe("Error while building CompanyAiAdoptionPgView: " + ex.getMessage());
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

    public CompanyAiAdoptionPgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}