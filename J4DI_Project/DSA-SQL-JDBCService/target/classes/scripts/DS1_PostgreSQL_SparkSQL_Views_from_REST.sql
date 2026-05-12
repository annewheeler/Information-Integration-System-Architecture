--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views_from_REST.sql
--- PostgreSQL SparkSQL Views using RESTEnabledSQLService + createJSONViewFromREST
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- CLEAN OLD PostgreSQL SparkSQL Views
--------------------------------------------------------------------------------

DROP VIEW IF EXISTS COMPANIES_PG_V;
DROP VIEW IF EXISTS COMPANY_AI_ADOPTION_PG_V;
DROP VIEW IF EXISTS COMPANY_AI_GOVERNANCE_PG_V;

DROP VIEW IF EXISTS COMPANIES_PG_JSON_VIEW;
DROP VIEW IF EXISTS COMPANY_AI_ADOPTION_PG_JSON_VIEW;
DROP VIEW IF EXISTS COMPANY_AI_GOVERNANCE_PG_JSON_VIEW;

--------------------------------------------------------------------------------
--- 1. PostgreSQL: COMPANIES_PG_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANIES_PG_JSON_VIEW',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompaniesPgView?limit=10000'
);

SELECT * FROM COMPANIES_PG_JSON_VIEW;

CREATE OR REPLACE VIEW COMPANIES_PG_V AS
SELECT v.*
FROM COMPANIES_PG_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANIES_PG_V;

SELECT *
FROM COMPANIES_PG_V
LIMIT 10;

--------------------------------------------------------------------------------
--- 2. PostgreSQL: COMPANY_AI_ADOPTION_PG_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANY_AI_ADOPTION_PG_JSON_VIEW',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiAdoptionPgView?limit=10000'
);

SELECT * FROM COMPANY_AI_ADOPTION_PG_JSON_VIEW;

CREATE OR REPLACE VIEW COMPANY_AI_ADOPTION_PG_V AS
SELECT v.*
FROM COMPANY_AI_ADOPTION_PG_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_AI_ADOPTION_PG_V;

SELECT *
FROM COMPANY_AI_ADOPTION_PG_V
LIMIT 10;

--------------------------------------------------------------------------------
--- 3. PostgreSQL: COMPANY_AI_GOVERNANCE_PG_V
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'COMPANY_AI_GOVERNANCE_PG_JSON_VIEW',
    'http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiGovernancePgView?limit=10000'
);

SELECT * FROM COMPANY_AI_GOVERNANCE_PG_JSON_VIEW;

CREATE OR REPLACE VIEW COMPANY_AI_GOVERNANCE_PG_V AS
SELECT v.*
FROM COMPANY_AI_GOVERNANCE_PG_JSON_VIEW json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT COUNT(*) AS cnt, COUNT(DISTINCT companyId) AS distinct_companies
FROM COMPANY_AI_GOVERNANCE_PG_V;

SELECT *
FROM COMPANY_AI_GOVERNANCE_PG_V
LIMIT 10;

--------------------------------------------------------------------------------
--- FINAL CHECK
--------------------------------------------------------------------------------

SHOW TABLES;