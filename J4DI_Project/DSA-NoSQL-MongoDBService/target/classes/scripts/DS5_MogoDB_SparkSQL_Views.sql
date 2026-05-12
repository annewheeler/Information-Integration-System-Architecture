--------------------------------------------------------------------------------
--- DS5_MogoDB_SparkSQL_Views.sql
--- Manual SparkSQL Views using QueryRESTDataService + from_json
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. MongoDB: MONGO_COUNTRY_AI_PROFILE_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryAiProfileView'
);

CREATE OR REPLACE VIEW MONGO_COUNTRY_AI_PROFILE_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<aiPatentFilings2024: BIGINT, aiResearchersPerMillion: DOUBLE, country: STRING, countryAiPolicy: STRING, digitalMaturityIndex: DOUBLE, gdpPerCapita: DOUBLE, internetPenetration: DOUBLE, region: STRING>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryAiProfileView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM MONGO_COUNTRY_AI_PROFILE_V_MANUAL;

--------------------------------------------------------------------------------
--- 2. MongoDB: MONGO_COUNTRY_INDUSTRY_BENCHMARK_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryIndustryBenchmarkView'
);

CREATE OR REPLACE VIEW MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<avgAiAdoptionRate: DOUBLE, avgAiFailureRate: DOUBLE, avgAiMaturityScore: DOUBLE, country: STRING, industry: STRING, region: STRING>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryIndustryBenchmarkView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_MANUAL;

--------------------------------------------------------------------------------
--- Check
--------------------------------------------------------------------------------

SHOW TABLES;