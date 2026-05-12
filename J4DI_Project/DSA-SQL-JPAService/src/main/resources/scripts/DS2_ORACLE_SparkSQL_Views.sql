--------------------------------------------------------------------------------
--- DS2_ORACLE_SparkSQL_Views.sql
--- Oracle SparkSQL Views using QueryRESTDataService + from_json
--------------------------------------------------------------------------------

DROP VIEW IF EXISTS COMPANIES_VIEW_MANUAL;
DROP VIEW IF EXISTS COMPANY_BUSINESS_OUTCOMES_VIEW_MANUAL;
DROP VIEW IF EXISTS COMPANY_WORKFORCE_IMPACT_VIEW_MANUAL;

--------------------------------------------------------------------------------
--- 1. Oracle: COMPANIES_VIEW_MANUAL
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW COMPANIES_VIEW_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<annualRevenueUsdMillions: DOUBLE, companyId: STRING, companySize: STRING, country: STRING, numEmployees: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyOrclView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANIES_VIEW_MANUAL;

SELECT *
FROM COMPANIES_VIEW_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- 2. Oracle: COMPANY_BUSINESS_OUTCOMES_VIEW_MANUAL
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW COMPANY_BUSINESS_OUTCOMES_VIEW_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<companyId: STRING, costReductionPercent: DOUBLE, customerSatisfaction: DOUBLE, innovationScore: DOUBLE, productivityChangePercent: DOUBLE, quarter: STRING, responseId: BIGINT, revenueGrowthPercent: DOUBLE, surveyYear: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyBusinessOutcomesOrclView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_BUSINESS_OUTCOMES_VIEW_MANUAL;

SELECT *
FROM COMPANY_BUSINESS_OUTCOMES_VIEW_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- 3. Oracle: COMPANY_WORKFORCE_IMPACT_VIEW_MANUAL
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW COMPANY_WORKFORCE_IMPACT_VIEW_MANUAL AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<companyId: STRING, employeeSatisfactionScore: DOUBLE, jobsCreated: BIGINT, jobsDisplaced: BIGINT, quarter: STRING, remoteWorkPercentage: DOUBLE, reskilledEmployees: BIGINT, responseId: BIGINT, surveyYear: BIGINT, taskAutomationRate: DOUBLE, timeSavedPerWeek: DOUBLE>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyWorkforceImpactOrclView?limit=10000'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_WORKFORCE_IMPACT_VIEW_MANUAL;

SELECT *
FROM COMPANY_WORKFORCE_IMPACT_VIEW_MANUAL
LIMIT 10;

--------------------------------------------------------------------------------
--- FINAL CHECK
--------------------------------------------------------------------------------

SHOW TABLES;