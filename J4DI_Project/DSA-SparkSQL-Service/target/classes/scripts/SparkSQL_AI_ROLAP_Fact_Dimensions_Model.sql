--------------------------------------------------------------------------------
--- SparkSQL_AI_ROLAP_Model.sql
--- Fact and Dimension Views
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. FACT_COMPANY_AI_EVENT_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW FACT_COMPANY_AI_EVENT_V AS
SELECT
    company_id,
    response_id,
    survey_year,
    quarter,

    country,
    industry,

    ai_adoption_rate,
    years_using_ai,
    num_ai_tools_used,
    ai_projects_active,

    ai_training_hours,
    ai_maturity_score,
    ai_failure_rate,
    ai_risk_management_score,

    productivity_change_percent,
    revenue_growth_percent,
    cost_reduction_percent,
    innovation_score,
    customer_satisfaction,

    remote_work_percentage,
    employee_satisfaction_score,
    task_automation_rate,
    time_saved_per_week,
    jobs_displaced,
    jobs_created,
    reskilled_employees
FROM CONS_PG_ORCL_EVENT_V;

SELECT COUNT(*)
FROM FACT_COMPANY_AI_EVENT_V;

SELECT *
FROM FACT_COMPANY_AI_EVENT_V
LIMIT 10;

--------------------------------------------------------------------------------
--- 2. DIM_TIME_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_TIME_V AS
SELECT DISTINCT
    survey_year,
    quarter,

    survey_year || ' ' || quarter AS year_quarter_label,

    survey_year * 10 +
        CASE
            WHEN UPPER(quarter) = 'Q1' THEN 1
            WHEN UPPER(quarter) = 'Q2' THEN 2
            WHEN UPPER(quarter) = 'Q3' THEN 3
            WHEN UPPER(quarter) = 'Q4' THEN 4
            ELSE 0
        END AS year_quarter_key

FROM FACT_COMPANY_AI_EVENT_V;

SELECT COUNT(*)
FROM DIM_TIME_V;

SELECT *
FROM DIM_TIME_V
ORDER BY year_quarter_key;
--------------------------------------------------------------------------------
--- 3. DIM_COMPANY_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_COMPANY_V AS
SELECT
    company_id,
    MIN(country) AS country,
    MIN(industry) AS industry,
    MIN(company_size) AS company_size,
    MIN(num_employees) AS num_employees,
    MIN(annual_revenue_usd_millions) AS annual_revenue_usd_millions
FROM CONS_PG_ORCL_EVENT_V
GROUP BY company_id;

SELECT COUNT(*)
FROM DIM_COMPANY_V;

SELECT *
FROM DIM_COMPANY_V
LIMIT 10;

--------------------------------------------------------------------------------
--- 4. DIM_COUNTRY_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_COUNTRY_V AS
SELECT DISTINCT
    country
FROM FACT_COMPANY_AI_EVENT_V;

SELECT COUNT(*)
FROM DIM_COUNTRY_V;

SELECT *
FROM DIM_COUNTRY_V
ORDER BY country;

--------------------------------------------------------------------------------
--- 5. DIM_INDUSTRY_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_INDUSTRY_V AS
SELECT DISTINCT
    industry
FROM FACT_COMPANY_AI_EVENT_V;

SELECT COUNT(*)
FROM DIM_INDUSTRY_V;

SELECT *
FROM DIM_INDUSTRY_V
ORDER BY industry;