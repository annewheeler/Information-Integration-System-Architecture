--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views.sql
--- PostgreSQL SparkSQL Views using QueryRESTDataService + from_json
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- CLEAN OLD MANUAL PostgreSQL SparkSQL Views
--------------------------------------------------------------------------------

DROP VIEW IF EXISTS COMPANIES_PG_V_MANUAL;
DROP VIEW IF EXISTS COMPANY_AI_ADOPTION_PG_V_MANUAL;
DROP VIEW IF EXISTS COMPANY_AI_GOVERNANCE_PG_V_MANUAL;

--------------------------------------------------------------------------------
--- 1. PostgreSQL: COMPANIES_PG_V_MANUAL
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompaniesPgView?limit=10000'
);

CREATE OR REPLACE VIEW COMPANIES_PG_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<companyId: STRING, companySize: STRING, country: STRING, industry: STRING>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompaniesPgView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANIES_PG_V_MANUAL;

SELECT *
FROM COMPANIES_PG_V_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- 2. PostgreSQL: COMPANY_AI_ADOPTION_PG_V_MANUAL
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW COMPANY_AI_ADOPTION_PG_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<aiAdoptionRate: DOUBLE, aiAdoptionStage: STRING, aiPrimaryTool: STRING, aiProjectsActive: BIGINT, aiUseCase: STRING, companyId: STRING, numAiToolsUsed: BIGINT, quarter: STRING, responseId: BIGINT, surveyYear: BIGINT, yearsUsingAi: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiAdoptionPgView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_AI_ADOPTION_PG_V_MANUAL;

SELECT *
FROM COMPANY_AI_ADOPTION_PG_V_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- 3. PostgreSQL: COMPANY_AI_GOVERNANCE_PG_V_MANUAL
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW COMPANY_AI_GOVERNANCE_PG_V_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<aiFailureRate: DOUBLE, aiMaturityScore: DOUBLE, aiRiskManagementScore: BIGINT, aiTrainingHours: BIGINT, companyId: STRING, quarter: STRING, responseId: BIGINT, surveyYear: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiGovernancePgView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_AI_GOVERNANCE_PG_V_MANUAL;

SELECT *
FROM COMPANY_AI_GOVERNANCE_PG_V_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- FINAL CHECK
--------------------------------------------------------------------------------

SHOW TABLES;