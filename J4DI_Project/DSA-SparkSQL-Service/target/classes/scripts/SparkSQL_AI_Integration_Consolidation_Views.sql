--------------------------------------------------------------------------------
--- SparkSQL_AI_Integration_Views.sql
--- Integration / Consolidation Views
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1. CONS_PG_ORCL_EVENT_V
--------------------------------------------------------------------------------
CREATE OR REPLACE VIEW CONS_PG_ORCL_EVENT_V AS
SELECT
    a.responseId AS response_id,
    a.companyId AS company_id,
    CAST(a.surveyYear AS INT) AS survey_year,
    TRIM(UPPER(a.quarter)) AS quarter,

    pgc.industry AS industry,
    COALESCE(oc.country, pgc.country) AS country,
    COALESCE(oc.companySize, pgc.companySize) AS company_size,

    oc.numEmployees AS num_employees,
    oc.annualRevenueUsdMillions AS annual_revenue_usd_millions,

    a.aiAdoptionRate AS ai_adoption_rate,
    a.aiAdoptionStage AS ai_adoption_stage,
    a.yearsUsingAi AS years_using_ai,
    a.aiPrimaryTool AS ai_primary_tool,
    a.numAiToolsUsed AS num_ai_tools_used,
    a.aiUseCase AS ai_use_case,
    a.aiProjectsActive AS ai_projects_active,

    g.aiTrainingHours AS ai_training_hours,
    g.aiMaturityScore AS ai_maturity_score,
    g.aiFailureRate AS ai_failure_rate,
    g.aiRiskManagementScore AS ai_risk_management_score,

    bo.productivityChangePercent AS productivity_change_percent,
    bo.revenueGrowthPercent AS revenue_growth_percent,
    bo.costReductionPercent AS cost_reduction_percent,
    bo.innovationScore AS innovation_score,
    bo.customerSatisfaction AS customer_satisfaction,

    wf.remoteWorkPercentage AS remote_work_percentage,
    wf.employeeSatisfactionScore AS employee_satisfaction_score,
    wf.taskAutomationRate AS task_automation_rate,
    wf.timeSavedPerWeek AS time_saved_per_week,
    wf.jobsDisplaced AS jobs_displaced,
    wf.jobsCreated AS jobs_created,
    wf.reskilledEmployees AS reskilled_employees

FROM COMPANY_AI_ADOPTION_PG_V a
INNER JOIN COMPANIES_PG_V pgc
    ON a.companyId = pgc.companyId
INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
    ON a.responseId = g.responseId
   AND a.companyId = g.companyId
   AND a.surveyYear = g.surveyYear
   AND TRIM(UPPER(a.quarter)) = TRIM(UPPER(g.quarter))
INNER JOIN COMPANIES_VIEW oc
    ON a.companyId = oc.companyId
INNER JOIN COMPANY_BUSINESS_OUTCOMES_VIEW bo
    ON a.responseId = bo.responseId
   AND a.companyId = bo.companyId
   AND a.surveyYear = bo.surveyYear
   AND TRIM(UPPER(a.quarter)) = TRIM(UPPER(bo.quarter))
INNER JOIN COMPANY_WORKFORCE_IMPACT_VIEW wf
    ON a.responseId = wf.responseId
   AND a.companyId = wf.companyId
   AND a.surveyYear = wf.surveyYear
   AND TRIM(UPPER(a.quarter)) = TRIM(UPPER(wf.quarter));

SELECT COUNT(*) FROM CONS_PG_ORCL_EVENT_V;

SELECT *
FROM CONS_PG_ORCL_EVENT_V;

SELECT
    SUM(CASE WHEN response_id IS NULL THEN 1 ELSE 0 END) AS null_response_id,
    SUM(CASE WHEN company_id IS NULL THEN 1 ELSE 0 END) AS null_company_id,
    SUM(CASE WHEN survey_year IS NULL THEN 1 ELSE 0 END) AS null_survey_year,
    SUM(CASE WHEN quarter IS NULL THEN 1 ELSE 0 END) AS null_quarter,
    SUM(CASE WHEN country IS NULL THEN 1 ELSE 0 END) AS null_country,
    SUM(CASE WHEN industry IS NULL THEN 1 ELSE 0 END) AS null_industry
FROM CONS_PG_ORCL_EVENT_V;

--------------------------------------------------------------------------------
--- 2. CONS_PG_MONGO_COUNTRY_INDUSTRY_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW CONS_PG_MONGO_COUNTRY_INDUSTRY_V AS
WITH PG_COUNTRY_INDUSTRY_AGG AS (
    SELECT
        cp.country AS country,
        cp.industry AS industry,
        ROUND(AVG(a.aiAdoptionRate), 2) AS pg_avg_ai_adoption_rate,
        ROUND(AVG(g.aiMaturityScore), 3) AS pg_avg_ai_maturity_score,
        ROUND(AVG(g.aiFailureRate), 3) AS pg_avg_ai_failure_rate,
        COUNT(*) AS pg_num_events
    FROM COMPANIES_PG_V cp
    INNER JOIN COMPANY_AI_ADOPTION_PG_V a
        ON cp.companyId = a.companyId
    INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
        ON a.responseId = g.responseId
       AND a.companyId = g.companyId
       AND a.surveyYear = g.surveyYear
       AND TRIM(UPPER(a.quarter)) = TRIM(UPPER(g.quarter))
    GROUP BY
        cp.country,
        cp.industry
)
SELECT
    m.country AS country,
    m.region AS region,
    m.industry AS industry,

    ROUND(m.avgAiAdoptionRate, 2) AS mongo_avg_ai_adoption_rate,
    ROUND(m.avgAiMaturityScore, 3) AS mongo_avg_ai_maturity_score,
    ROUND(m.avgAiFailureRate, 3) AS mongo_avg_ai_failure_rate,

    p.pg_avg_ai_adoption_rate AS pg_avg_ai_adoption_rate,
    p.pg_avg_ai_maturity_score AS pg_avg_ai_maturity_score,
    p.pg_avg_ai_failure_rate AS pg_avg_ai_failure_rate,
    p.pg_num_events AS pg_num_events,

    ROUND(p.pg_avg_ai_adoption_rate - m.avgAiAdoptionRate, 2) AS diff_ai_adoption_rate,
    ROUND(p.pg_avg_ai_maturity_score - m.avgAiMaturityScore, 3) AS diff_ai_maturity_score,
    ROUND(p.pg_avg_ai_failure_rate - m.avgAiFailureRate, 3) AS diff_ai_failure_rate

FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_V m
LEFT JOIN PG_COUNTRY_INDUSTRY_AGG p
    ON TRIM(UPPER(m.country)) = TRIM(UPPER(p.country))
   AND TRIM(UPPER(m.industry)) = TRIM(UPPER(p.industry));

SELECT COUNT(*) FROM CONS_PG_MONGO_COUNTRY_INDUSTRY_V;
SELECT * FROM CONS_PG_MONGO_COUNTRY_INDUSTRY_V LIMIT 10;


--------------------------------------------------------------------------------
--- 3. CONS_PG_XLS_COUNTRY_TIME_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW CONS_PG_XLS_COUNTRY_TIME_V AS
WITH PG_COUNTRY_TIME_AGG AS (
    SELECT
        cp.country AS country,
        a.surveyYear AS survey_year,
        TRIM(UPPER(a.quarter)) AS quarter_label,
        COUNT(DISTINCT a.companyId) AS pg_num_companies,
        ROUND(AVG(a.aiAdoptionRate), 2) AS pg_avg_ai_adoption_rate,
        ROUND(AVG(g.aiMaturityScore), 3) AS pg_avg_ai_maturity_score
    FROM COMPANIES_PG_V cp
    INNER JOIN COMPANY_AI_ADOPTION_PG_V a
        ON cp.companyId = a.companyId
    INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
        ON a.responseId = g.responseId
       AND a.companyId = g.companyId
       AND a.surveyYear = g.surveyYear
       AND TRIM(UPPER(a.quarter)) = TRIM(UPPER(g.quarter))
    GROUP BY
        cp.country,
        a.surveyYear,
        TRIM(UPPER(a.quarter))
),
XLS_COUNTRY_TIME AS (
    SELECT
        country AS country,
        surveyYear AS survey_year,
        TRIM(UPPER(quarter)) AS quarter_label,
        numCompanies AS num_companies,
        avgAiAdoptionRate AS avg_ai_adoption_rate,
        avgAiMaturityScore AS avg_ai_maturity_score,
        avgProductivityChangePercent AS avg_productivity_change_percent,
        totalJobsDisplaced AS total_jobs_displaced,
        totalJobsCreated AS total_jobs_created
    FROM XLS_COUNTRY_QUARTER_SUMMARY_V
)
SELECT
    x.country AS country,
    x.survey_year AS survey_year,
    x.quarter_label AS quarter,

    x.num_companies AS xls_num_companies,
    x.avg_ai_adoption_rate AS xls_avg_ai_adoption_rate,
    x.avg_ai_maturity_score AS xls_avg_ai_maturity_score,
    x.avg_productivity_change_percent AS xls_avg_productivity_change_percent,
    x.total_jobs_displaced AS xls_total_jobs_displaced,
    x.total_jobs_created AS xls_total_jobs_created,

    p.pg_num_companies AS pg_num_companies,
    p.pg_avg_ai_adoption_rate AS pg_avg_ai_adoption_rate,
    p.pg_avg_ai_maturity_score AS pg_avg_ai_maturity_score,

    (p.pg_num_companies - x.num_companies) AS diff_num_companies,
    ROUND(p.pg_avg_ai_adoption_rate - x.avg_ai_adoption_rate, 2) AS diff_ai_adoption_rate,
    ROUND(p.pg_avg_ai_maturity_score - x.avg_ai_maturity_score, 3) AS diff_ai_maturity_score

FROM XLS_COUNTRY_TIME x
LEFT JOIN PG_COUNTRY_TIME_AGG p
    ON TRIM(UPPER(x.country)) = TRIM(UPPER(p.country))
   AND x.survey_year = p.survey_year
   AND TRIM(UPPER(x.quarter_label)) = TRIM(UPPER(p.quarter_label));

SELECT COUNT(*) FROM CONS_PG_XLS_COUNTRY_TIME_V;
SELECT * FROM CONS_PG_XLS_COUNTRY_TIME_V LIMIT 10;
SELECT * FROM CONS_PG_XLS_COUNTRY_TIME_V WHERE pg_num_companies IS NOT NULL LIMIT 10;


--------------------------------------------------------------------------------
--- 4. CONS_ORCL_PG_XLS_COUNTRY_TIME_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW CONS_ORCL_PG_XLS_COUNTRY_TIME_V AS
WITH EVENT_COUNTRY_TIME_AGG AS (
    SELECT
        country AS country,
        survey_year AS survey_year,
        TRIM(UPPER(quarter)) AS quarter_label,
        COUNT(DISTINCT company_id) AS event_num_companies,
        ROUND(AVG(ai_adoption_rate), 2) AS event_avg_ai_adoption_rate,
        ROUND(AVG(ai_maturity_score), 3) AS event_avg_ai_maturity_score,
        ROUND(AVG(productivity_change_percent), 2) AS event_avg_productivity_change_percent,
        SUM(COALESCE(jobs_displaced, 0)) AS event_total_jobs_displaced,
        SUM(COALESCE(jobs_created, 0)) AS event_total_jobs_created
    FROM CONS_PG_ORCL_EVENT_V
    GROUP BY
        country,
        survey_year,
        TRIM(UPPER(quarter))
),
XLS_COUNTRY_TIME AS (
    SELECT
        country AS country,
        surveyYear AS survey_year,
        TRIM(UPPER(quarter)) AS quarter_label,
        numCompanies AS num_companies,
        avgAiAdoptionRate AS avg_ai_adoption_rate,
        avgAiMaturityScore AS avg_ai_maturity_score,
        avgProductivityChangePercent AS avg_productivity_change_percent,
        totalJobsDisplaced AS total_jobs_displaced,
        totalJobsCreated AS total_jobs_created
    FROM XLS_COUNTRY_QUARTER_SUMMARY_V
)
SELECT
    x.country AS country,
    x.survey_year AS survey_year,
    x.quarter_label AS quarter,

    x.num_companies AS xls_num_companies,
    x.avg_ai_adoption_rate AS xls_avg_ai_adoption_rate,
    x.avg_ai_maturity_score AS xls_avg_ai_maturity_score,
    x.avg_productivity_change_percent AS xls_avg_productivity_change_percent,
    x.total_jobs_displaced AS xls_total_jobs_displaced,
    x.total_jobs_created AS xls_total_jobs_created,

    e.event_num_companies AS event_num_companies,
    e.event_avg_ai_adoption_rate AS event_avg_ai_adoption_rate,
    e.event_avg_ai_maturity_score AS event_avg_ai_maturity_score,
    e.event_avg_productivity_change_percent AS event_avg_productivity_change_percent,
    e.event_total_jobs_displaced AS event_total_jobs_displaced,
    e.event_total_jobs_created AS event_total_jobs_created,

    (e.event_num_companies - x.num_companies) AS diff_num_companies,
    ROUND(e.event_avg_ai_adoption_rate - x.avg_ai_adoption_rate, 2) AS diff_ai_adoption_rate,
    ROUND(e.event_avg_ai_maturity_score - x.avg_ai_maturity_score, 3) AS diff_ai_maturity_score,
    ROUND(e.event_avg_productivity_change_percent - x.avg_productivity_change_percent, 2) AS diff_productivity_change_percent,
    (e.event_total_jobs_displaced - x.total_jobs_displaced) AS diff_total_jobs_displaced,
    (e.event_total_jobs_created - x.total_jobs_created) AS diff_total_jobs_created

FROM XLS_COUNTRY_TIME x
LEFT JOIN EVENT_COUNTRY_TIME_AGG e
    ON TRIM(UPPER(x.country)) = TRIM(UPPER(e.country))
   AND x.survey_year = e.survey_year
   AND TRIM(UPPER(x.quarter_label)) = TRIM(UPPER(e.quarter_label));

SELECT COUNT(*) FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V;
SELECT * FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V LIMIT 10;
SELECT * FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V WHERE event_num_companies IS NOT NULL LIMIT 10;


--------------------------------------------------------------------------------
--- 5. CONS_GLOBAL_COUNTRY_TIME_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW CONS_GLOBAL_COUNTRY_TIME_V AS
SELECT
    c.country AS country,
    c.survey_year AS survey_year,
    c.quarter AS quarter,

    c.xls_num_companies AS xls_num_companies,
    c.xls_avg_ai_adoption_rate AS xls_avg_ai_adoption_rate,
    c.xls_avg_ai_maturity_score AS xls_avg_ai_maturity_score,
    c.xls_avg_productivity_change_percent AS xls_avg_productivity_change_percent,
    c.xls_total_jobs_displaced AS xls_total_jobs_displaced,
    c.xls_total_jobs_created AS xls_total_jobs_created,

    c.event_num_companies AS event_num_companies,
    c.event_avg_ai_adoption_rate AS event_avg_ai_adoption_rate,
    c.event_avg_ai_maturity_score AS event_avg_ai_maturity_score,
    c.event_avg_productivity_change_percent AS event_avg_productivity_change_percent,
    c.event_total_jobs_displaced AS event_total_jobs_displaced,
    c.event_total_jobs_created AS event_total_jobs_created,

    c.diff_num_companies AS diff_num_companies,
    c.diff_ai_adoption_rate AS diff_ai_adoption_rate,
    c.diff_ai_maturity_score AS diff_ai_maturity_score,
    c.diff_productivity_change_percent AS diff_productivity_change_percent,
    c.diff_total_jobs_displaced AS diff_total_jobs_displaced,
    c.diff_total_jobs_created AS diff_total_jobs_created,

    m.region AS region,
    ROUND(m.gdpPerCapita, 2) AS gdp_per_capita,
    ROUND(m.internetPenetration, 2) AS internet_penetration,
    ROUND(m.digitalMaturityIndex, 3) AS digital_maturity_index,
    m.countryAiPolicy AS country_ai_policy,
    m.aiPatentFilings2024 AS ai_patent_filings_2024,
    ROUND(m.aiResearchersPerMillion, 2) AS ai_researchers_per_million

FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V c
LEFT JOIN MONGO_COUNTRY_AI_PROFILE_V m
    ON TRIM(UPPER(c.country)) = TRIM(UPPER(m.country));

SELECT COUNT(*) FROM CONS_GLOBAL_COUNTRY_TIME_V;
SELECT * FROM CONS_GLOBAL_COUNTRY_TIME_V LIMIT 10;
SELECT * FROM CONS_GLOBAL_COUNTRY_TIME_V WHERE event_num_companies IS NOT NULL LIMIT 10;


--------------------------------------------------------------------------------
--- FINAL CHECK
--------------------------------------------------------------------------------

SELECT 'CONS_PG_ORCL_EVENT_V' AS view_name, COUNT(*) AS row_count
FROM CONS_PG_ORCL_EVENT_V
UNION ALL
SELECT 'CONS_PG_MONGO_COUNTRY_INDUSTRY_V', COUNT(*)
FROM CONS_PG_MONGO_COUNTRY_INDUSTRY_V
UNION ALL
SELECT 'CONS_PG_XLS_COUNTRY_TIME_V', COUNT(*)
FROM CONS_PG_XLS_COUNTRY_TIME_V
UNION ALL
SELECT 'CONS_ORCL_PG_XLS_COUNTRY_TIME_V', COUNT(*)
FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V
UNION ALL
SELECT 'CONS_GLOBAL_COUNTRY_TIME_V', COUNT(*)
FROM CONS_GLOBAL_COUNTRY_TIME_V;