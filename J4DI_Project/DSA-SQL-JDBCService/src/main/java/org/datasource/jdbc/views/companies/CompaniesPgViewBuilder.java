package org.datasource.jdbc.views.companies;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
    Builder pentru tabela PostgreSQL: public.companies_pg
*/
@Service
public class CompaniesPgViewBuilder {

    private static final Logger logger = Logger.getLogger(CompaniesPgViewBuilder.class.getName());

    // Numărul implicit de rânduri returnate când endpointul este apelat fără ?limit=
    private static final int DEFAULT_LIMIT = 1000;

    // Limită maximă pentru a evita blocarea browserului, IntelliJ sau SparkSQL
    private static final int MAX_LIMIT = 7000;

    /*
        SQL Map:
        Selectăm coloanele din tabela PostgreSQL.

        ORDER BY MD5(...) produce o ordine pseudo-random deterministă:
        - nu ia doar primele companii din tabel
        - dar returnează aceeași selecție la fiecare rulare
    */
    private String SQL_COMPANIES_PG_SELECT =
            "SELECT company_id, industry, country, company_size " +
                    "FROM public.companies_pg " +
                    "ORDER BY MD5(CAST(company_id AS TEXT) || 'IIS_SAMPLE_2026') " +
                    "LIMIT ?";

    // DataCache: lista returnată de endpoint
    private List<CompaniesPgView> companiesPgViewList = new ArrayList<>();

    public List<CompaniesPgView> getViewList() {
        return this.companiesPgViewList;
    }

    /*
        Metoda standard, compatibilă cu modelul profesorului.
        Dacă RESTViewServiceJDBC apelează build() fără parametri,
        se returnează DEFAULT_LIMIT rânduri.
    */
    public CompaniesPgViewBuilder build() {
        return this.build(DEFAULT_LIMIT);
    }

    /*
        Metodă cu limită controlată.
        Este apelată când endpointul primește parametru ?limit=...
    */
    public CompaniesPgViewBuilder build(Integer limit) {
        int safeLimit = normalizeLimit(limit);

        logger.info(">>> Building CompaniesPgView with limit = " + safeLimit);

        try (Connection jdbcConnection = jdbcConnector.getConnection();
             PreparedStatement selectStmt = jdbcConnection.prepareStatement(SQL_COMPANIES_PG_SELECT)) {

            // Setăm valoarea pentru LIMIT ?
            selectStmt.setInt(1, safeLimit);

            ResultSet rs = selectStmt.executeQuery();

            companiesPgViewList = new ArrayList<>();

            while (rs.next()) {
                String company_id = rs.getString("company_id");
                String industry = rs.getString("industry");
                String country = rs.getString("country");
                String company_size = rs.getString("company_size");

                this.companiesPgViewList.add(
                        new CompaniesPgView(company_id, industry, country, company_size)
                );
            }

        } catch (Exception ex) {
            logger.severe("Error while building CompaniesPgView: " + ex.getMessage());
            ex.printStackTrace();
        }

        return this;
    }

    /*
        Normalizează limita cerută:
        - dacă nu există sau este <= 0, folosim 1000
        - dacă depășește 7000, o tăiem la 7000
    */
    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    /* JDBC Session Management ---------------------------------------- */

    private JDBCDataSourceConnector jdbcConnector;

    public CompaniesPgViewBuilder(JDBCDataSourceConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }
}