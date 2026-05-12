--------------------------------------------------------------------------------
--- DS_XLS_SparkSQL_Views_from_REST.sql
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. Excel: XLS_COUNTRY_QUARTER_SUMMARY_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'XLS_COUNTRY_QUARTER_SUMMARY_JSON_VIEW',
    'http://localhost:8094/DSA-DOC-XLSService/rest/reporting/CountryQuarterSummaryView'
);

SELECT * FROM XLS_COUNTRY_QUARTER_SUMMARY_JSON_VIEW;

CREATE OR REPLACE VIEW XLS_COUNTRY_QUARTER_SUMMARY_V AS
SELECT v.*
FROM XLS_COUNTRY_QUARTER_SUMMARY_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM XLS_COUNTRY_QUARTER_SUMMARY_V;