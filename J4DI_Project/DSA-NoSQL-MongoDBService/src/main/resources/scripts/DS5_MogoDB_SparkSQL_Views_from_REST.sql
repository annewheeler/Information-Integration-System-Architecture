--------------------------------------------------------------------------------
--- DS5_MogoDB_SparkSQL_Views_from_REST.sql
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. MongoDB: MONGO_COUNTRY_AI_PROFILE_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'MONGO_COUNTRY_AI_PROFILE_JSON_VIEW',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryAiProfileView'
);

SELECT * FROM MONGO_COUNTRY_AI_PROFILE_JSON_VIEW;

CREATE OR REPLACE VIEW MONGO_COUNTRY_AI_PROFILE_V AS
SELECT v.*
FROM MONGO_COUNTRY_AI_PROFILE_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM MONGO_COUNTRY_AI_PROFILE_V;

--------------------------------------------------------------------------------
--- 2. MongoDB: MONGO_COUNTRY_INDUSTRY_BENCHMARK_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'MONGO_COUNTRY_INDUSTRY_BENCHMARK_JSON_VIEW',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryIndustryBenchmarkView'
);

SELECT * FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_JSON_VIEW;

CREATE OR REPLACE VIEW MONGO_COUNTRY_INDUSTRY_BENCHMARK_V AS
SELECT v.*
FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_V;