--------------------------------------------------------------------------------
--- DS_XLS_SparkSQL_Views.sql
--- Manual SparkSQL Views using QueryRESTDataService + from_json
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. Excel: XLS_COUNTRY_QUARTER_SUMMARY_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8094/DSA-DOC-XLSService/rest/reporting/CountryQuarterSummaryView'
);

CREATE OR REPLACE VIEW XLS_COUNTRY_QUARTER_SUMMARY_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<avgAiAdoptionRate: DOUBLE, avgAiMaturityScore: DOUBLE, avgProductivityChangePercent: DOUBLE, country: STRING, numCompanies: BIGINT, quarter: STRING, surveyYear: BIGINT, totalJobsCreated: BIGINT, totalJobsDisplaced: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8094/DSA-DOC-XLSService/rest/reporting/CountryQuarterSummaryView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM XLS_COUNTRY_QUARTER_SUMMARY_V_MANUAL;

--------------------------------------------------------------------------------
--- Check
--------------------------------------------------------------------------------

SHOW TABLES;