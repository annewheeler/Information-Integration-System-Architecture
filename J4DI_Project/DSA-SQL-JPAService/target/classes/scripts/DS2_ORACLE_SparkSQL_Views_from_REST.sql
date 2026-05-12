--------------------------------------------------------------------------------
--- DS2_ORACLE_SparkSQL_Views_from_REST.sql
--- Oracle SparkSQL Views using RESTEnabledSQLService + createJSONViewFromREST
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- CLEAN OLD Oracle SparkSQL Views
--------------------------------------------------------------------------------

DROP VIEW IF EXISTS COMPANIES_VIEW;
DROP VIEW IF EXISTS COMPANY_BUSINESS_OUTCOMES_VIEW;
DROP VIEW IF EXISTS COMPANY_WORKFORCE_IMPACT_VIEW;

DROP VIEW IF EXISTS COMPANIES_ORCL_JSON_VIEW;
DROP VIEW IF EXISTS COMPANY_BUSINESS_OUTCOMES_ORCL_JSON_VIEW;
DROP VIEW IF EXISTS COMPANY_WORKFORCE_IMPACT_ORCL_JSON_VIEW;

--------------------------------------------------------------------------------
--- 1. Oracle: COMPANIES_VIEW
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANIES_ORCL_JSON_VIEW',
    'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyOrclView?limit=10000'
);

CREATE OR REPLACE VIEW COMPANIES_VIEW AS
SELECT v.*
FROM COMPANIES_ORCL_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANIES_VIEW;

SELECT *
FROM COMPANIES_VIEW
LIMIT 10;

--------------------------------------------------------------------------------
--- 2. Oracle: COMPANY_BUSINESS_OUTCOMES_VIEW
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANY_BUSINESS_OUTCOMES_ORCL_JSON_VIEW',
    'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyBusinessOutcomesOrclView?limit=10000'
);

CREATE OR REPLACE VIEW COMPANY_BUSINESS_OUTCOMES_VIEW AS
SELECT v.*
FROM COMPANY_BUSINESS_OUTCOMES_ORCL_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_BUSINESS_OUTCOMES_VIEW;

SELECT *
FROM COMPANY_BUSINESS_OUTCOMES_VIEW
LIMIT 10;

--------------------------------------------------------------------------------
--- 3. Oracle: COMPANY_WORKFORCE_IMPACT_VIEW
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANY_WORKFORCE_IMPACT_ORCL_JSON_VIEW',
    'http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyWorkforceImpactOrclView?limit=10000'
);

CREATE OR REPLACE VIEW COMPANY_WORKFORCE_IMPACT_VIEW AS
SELECT v.*
FROM COMPANY_WORKFORCE_IMPACT_ORCL_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_WORKFORCE_IMPACT_VIEW;

SELECT *
FROM COMPANY_WORKFORCE_IMPACT_VIEW
LIMIT 10;

--------------------------------------------------------------------------------
--- FINAL CHECK
--------------------------------------------------------------------------------

SHOW TABLES;