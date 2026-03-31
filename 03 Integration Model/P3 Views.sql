--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
                                                --  CONSOLIDATION VIEWS
--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
-- CONS_PG_ORCL_EVENT_V consolidation view de bază
-- integrare directă între 2 surse
-- company_id + response_id + survey_year + quarter
/* Scop: să aducă într-un singur loc:
profilul companiei,
adopția AI,
guvernanța AI,
outcome-urile de business,
impactul în workforce 
PostgreSQL wrapper views
COMPANIES_PG_V
COMPANY_AI_ADOPTION_PG_V
COMPANY_AI_GOVERNANCE_PG_V
Oracle wrapper views
COMPANIES_VIEW
COMPANY_BUSINESS_OUTCOMES_VIEW
COMPANY_WORKFORCE_IMPACT_VIEW */ 

CREATE OR REPLACE VIEW CONS_PG_ORCL_EVENT_V AS
SELECT
    a."response_id"                    AS response_id,
    a."company_id"                     AS company_id,
    a."survey_year"                    AS survey_year,
    a."quarter"                        AS quarter,

    pgc."industry"                     AS industry,
    COALESCE(oc.country, pgc."country") AS country,
    COALESCE(oc.company_size, pgc."company_size") AS company_size,

    oc.num_employees                   AS num_employees,
    oc.annual_revenue_usd_millions     AS annual_revenue_usd_millions,

    a."ai_adoption_rate"               AS ai_adoption_rate,
    a."ai_adoption_stage"              AS ai_adoption_stage,
    a."years_using_ai"                 AS years_using_ai,
    a."ai_primary_tool"                AS ai_primary_tool,
    a."num_ai_tools_used"              AS num_ai_tools_used,
    a."ai_use_case"                    AS ai_use_case,
    a."ai_projects_active"             AS ai_projects_active,

    g."ai_training_hours"              AS ai_training_hours,
    g."ai_maturity_score"              AS ai_maturity_score,
    g."ai_failure_rate"                AS ai_failure_rate,
    g."ai_risk_management_score"       AS ai_risk_management_score,

    bo.productivity_change_percent     AS productivity_change_percent,
    bo.revenue_growth_percent          AS revenue_growth_percent,
    bo.cost_reduction_percent          AS cost_reduction_percent,
    bo.innovation_score                AS innovation_score,
    bo.customer_satisfaction           AS customer_satisfaction,

    wf.remote_work_percentage          AS remote_work_percentage,
    wf.employee_satisfaction_score     AS employee_satisfaction_score,
    wf.task_automation_rate            AS task_automation_rate,
    wf.time_saved_per_week             AS time_saved_per_week,
    wf.jobs_displaced                  AS jobs_displaced,
    wf.jobs_created                    AS jobs_created,
    wf.reskilled_employees             AS reskilled_employees

FROM COMPANY_AI_ADOPTION_PG_V a
INNER JOIN COMPANIES_PG_V pgc
    ON a."company_id" = pgc."company_id"
INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
    ON a."response_id" = g."response_id"
   AND a."company_id" = g."company_id"
   AND a."survey_year" = g."survey_year"
   AND a."quarter" = g."quarter"
INNER JOIN COMPANIES_VIEW oc
    ON a."company_id" = oc.company_id
INNER JOIN COMPANY_BUSINESS_OUTCOMES_VIEW bo
    ON a."response_id" = bo.response_id
   AND a."company_id" = bo.company_id
   AND a."survey_year" = bo.survey_year
   AND a."quarter" = bo.quarter
INNER JOIN COMPANY_WORKFORCE_IMPACT_VIEW wf
    ON a."response_id" = wf.response_id
   AND a."company_id" = wf.company_id
   AND a."survey_year" = wf.survey_year
   AND a."quarter" = wf.quarter;

--testare view
   SELECT *
FROM CONS_PG_ORCL_EVENT_V
FETCH FIRST 20 ROWS ONLY;

SELECT
    company_id,
    response_id,
    survey_year,
    quarter,
    country,
    industry,
    ai_adoption_rate,
    ai_maturity_score,
    productivity_change_percent,
    jobs_created
FROM CONS_PG_ORCL_EVENT_V
FETCH FIRST 20 ROWS ONLY;


-- CONS_PG_MONGO_COUNTRY_INDUSTRY_V consolidation view de bază
-- integrare directă între 2 surse
-- country + industry
/* Scop: să aducă într-un singur loc:
benchmark-urile externe Mongo la nivel de țară și industrie,
mediile calculate din datele PostgreSQL,
diferențele dintre benchmark și datele reale observate
PostgreSQL wrapper views
COMPANIES_PG_V
COMPANY_AI_ADOPTION_PG_V
COMPANY_AI_GOVERNANCE_PG_V
Mongo wrapper views
MONGO_COUNTRY_AI_BENCHMARKS_V */

CREATE OR REPLACE VIEW CONS_PG_MONGO_COUNTRY_INDUSTRY_V AS
WITH PG_COUNTRY_INDUSTRY_AGG AS (
    SELECT
        cp."country"                               AS country,
        cp."industry"                              AS industry,
        ROUND(AVG(a."ai_adoption_rate"), 2)        AS pg_avg_ai_adoption_rate,
        ROUND(AVG(g."ai_maturity_score"), 3)       AS pg_avg_ai_maturity_score,
        ROUND(AVG(g."ai_failure_rate"), 3)         AS pg_avg_ai_failure_rate,
        COUNT(*)                                   AS pg_num_events
    FROM COMPANIES_PG_V cp
    INNER JOIN COMPANY_AI_ADOPTION_PG_V a
        ON cp."company_id" = a."company_id"
    INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
        ON a."response_id" = g."response_id"
       AND a."company_id" = g."company_id"
       AND a."survey_year" = g."survey_year"
       AND a."quarter" = g."quarter"
    GROUP BY
        cp."country",
        cp."industry"
)
SELECT
    m.country                                      AS country,
    m.region                                       AS region,
    m.industry                                     AS industry,

    ROUND(m.avg_ai_adoption_rate, 2)               AS mongo_avg_ai_adoption_rate,
    ROUND(m.avg_ai_maturity_score, 3)              AS mongo_avg_ai_maturity_score,
    ROUND(m.avg_ai_failure_rate, 3)                AS mongo_avg_ai_failure_rate,

    p.pg_avg_ai_adoption_rate                      AS pg_avg_ai_adoption_rate,
    p.pg_avg_ai_maturity_score                     AS pg_avg_ai_maturity_score,
    p.pg_avg_ai_failure_rate                       AS pg_avg_ai_failure_rate,
    p.pg_num_events                                AS pg_num_events,

    ROUND(p.pg_avg_ai_adoption_rate - m.avg_ai_adoption_rate, 2)   AS diff_ai_adoption_rate,
    ROUND(p.pg_avg_ai_maturity_score - m.avg_ai_maturity_score, 3) AS diff_ai_maturity_score,
    ROUND(p.pg_avg_ai_failure_rate - m.avg_ai_failure_rate, 3)     AS diff_ai_failure_rate
FROM MONGO_COUNTRY_AI_BENCHMARKS_V m
LEFT JOIN PG_COUNTRY_INDUSTRY_AGG p
    ON TRIM(UPPER(m.country)) = TRIM(UPPER(p.country))
   AND TRIM(UPPER(m.industry)) = TRIM(UPPER(p.industry));
   
   --test
   SELECT *
FROM CONS_PG_MONGO_COUNTRY_INDUSTRY_V
FETCH FIRST 20 ROWS ONLY;

-- CONS_PG_XLS_COUNTRY_TIME_V consolidation view de bază
-- integrare directă între 2 surse
-- country + survey_year + quarter
/* Scop: să aducă într-un singur loc:
agregatele calculate din datele PostgreSQL la nivel de țară și perioadă,
valorile agregate deja existente în Excel,
diferențele dintre valorile PostgreSQL și cele din Excel
PostgreSQL wrapper views
COMPANIES_PG_V
COMPANY_AI_ADOPTION_PG_V
COMPANY_AI_GOVERNANCE_PG_V
Excel wrapper views
XLS_COUNTRY_QUARTER_SUMMARY_V */

CREATE OR REPLACE VIEW CONS_PG_XLS_COUNTRY_TIME_V AS
WITH PG_COUNTRY_TIME_AGG AS (
    SELECT
        cp."country" AS country,
        a."survey_year" AS survey_year,
        a."quarter" AS quarter_label,
        COUNT(DISTINCT a."company_id") AS pg_num_companies,
        ROUND(AVG(a."ai_adoption_rate"), 2) AS pg_avg_ai_adoption_rate,
        ROUND(AVG(g."ai_maturity_score"), 3) AS pg_avg_ai_maturity_score
    FROM COMPANIES_PG_V cp
    INNER JOIN COMPANY_AI_ADOPTION_PG_V a
        ON cp."company_id" = a."company_id"
    INNER JOIN COMPANY_AI_GOVERNANCE_PG_V g
        ON a."response_id" = g."response_id"
       AND a."company_id" = g."company_id"
       AND a."survey_year" = g."survey_year"
       AND a."quarter" = g."quarter"
    GROUP BY
        cp."country",
        a."survey_year",
        a."quarter"
),
XLS_COUNTRY_TIME AS (
    SELECT
        "country" AS country,
        "survey_year" AS survey_year,
        "quarter" AS quarter_label,
        "num_companies" AS num_companies,
        "avg_ai_adoption_rate" AS avg_ai_adoption_rate,
        "avg_ai_maturity_score" AS avg_ai_maturity_score,
        "avg_productivity_change_percent" AS avg_productivity_change_percent,
        "total_jobs_displaced" AS total_jobs_displaced,
        "total_jobs_created" AS total_jobs_created
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
   AND TRIM(UPPER(x.quarter_label)) = TRIM(UPPER(p.quarter_label))
   ;
   
   --test
   SELECT *
FROM CONS_PG_XLS_COUNTRY_TIME_V
FETCH FIRST 20 ROWS ONLY;

-- CONS_ORCL_PG_XLS_COUNTRY_TIME_V consolidation view derivat
-- integrare progresivă între un view consolidat și o sursă agregată
-- country + survey_year + quarter
/* Scop: să aducă într-un singur loc:
agregatele calculate din view-ul CONS_PG_ORCL_EVENT_V la nivel de țară și perioadă,
valorile agregate deja existente în Excel,
diferențele dintre valorile consolidate PG+Oracle și cele din Excel
Consolidation views
CONS_PG_ORCL_EVENT_V
Excel wrapper views
XLS_COUNTRY_QUARTER_SUMMARY_V */

CREATE OR REPLACE VIEW CONS_ORCL_PG_XLS_COUNTRY_TIME_V AS
WITH EVENT_COUNTRY_TIME_AGG AS (
    SELECT
        country AS country,
        survey_year AS survey_year,
        quarter AS quarter_label,
        COUNT(DISTINCT company_id) AS event_num_companies,
        ROUND(AVG(ai_adoption_rate), 2) AS event_avg_ai_adoption_rate,
        ROUND(AVG(ai_maturity_score), 3) AS event_avg_ai_maturity_score,
        ROUND(AVG(productivity_change_percent), 2) AS event_avg_productivity_change_percent,
        SUM(NVL(jobs_displaced, 0)) AS event_total_jobs_displaced,
        SUM(NVL(jobs_created, 0)) AS event_total_jobs_created
    FROM CONS_PG_ORCL_EVENT_V
    GROUP BY
        country,
        survey_year,
        quarter
),
XLS_COUNTRY_TIME AS (
    SELECT
        "country" AS country,
        "survey_year" AS survey_year,
        "quarter" AS quarter_label,
        "num_companies" AS num_companies,
        "avg_ai_adoption_rate" AS avg_ai_adoption_rate,
        "avg_ai_maturity_score" AS avg_ai_maturity_score,
        "avg_productivity_change_percent" AS avg_productivity_change_percent,
        "total_jobs_displaced" AS total_jobs_displaced,
        "total_jobs_created" AS total_jobs_created
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
   
 --test  
   SELECT *
FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V
FETCH FIRST 20 ROWS ONLY;

-- CONS_GLOBAL_COUNTRY_TIME_V consolidation view derivat final
-- integrare progresivă între un view consolidat multi-sursă și contextul macro Mongo
-- country + survey_year + quarter
/* Scop: să aducă într-un singur loc:
agregatele consolidate PG + Oracle + Excel la nivel de țară și perioadă,
contextul macroeconomic și AI ecosystem din Mongo la nivel de țară,
o bază unificată pentru modelul analitic ulterior
Consolidation views
CONS_ORCL_PG_XLS_COUNTRY_TIME_V
Mongo wrapper views
MONGO_COUNTRY_AI_PROFILE_V */

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
    ROUND(m.gdp_per_capita, 2) AS gdp_per_capita,
    ROUND(m.internet_penetration, 2) AS internet_penetration,
    ROUND(m.digital_maturity_index, 3) AS digital_maturity_index,
    m.country_ai_policy AS country_ai_policy,
    m.ai_patent_filings_2024 AS ai_patent_filings_2024,
    ROUND(m.ai_researchers_per_million, 2) AS ai_researchers_per_million

FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V c
LEFT JOIN MONGO_COUNTRY_AI_PROFILE_V m
    ON TRIM(UPPER(c.country)) = TRIM(UPPER(m.country));
    
    SELECT *
FROM CONS_GLOBAL_COUNTRY_TIME_V
FETCH FIRST 20 ROWS ONLY;

--------------

SELECT DISTINCT country
FROM MONGO_COUNTRY_AI_PROFILE_V
ORDER BY country;

--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
                                                --  FACT VIEW
--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------

-- FACT_COMPANY_AI_EVENT_V fact view central
-- view de fapte pentru modelul analitic ROLAP
-- company_id + response_id + survey_year + quarter
/* Scop: să stocheze la nivel de eveniment analitic:
cheile principale de analiză,
măsurile cantitative agregabile,
baza centrală pentru dimensiuni și view-uri OLAP
Consolidation views
CONS_PG_ORCL_EVENT_V */

CREATE OR REPLACE VIEW FACT_COMPANY_AI_EVENT_V AS
SELECT
    company_id AS company_id,
    response_id AS response_id,
    survey_year AS survey_year,
    quarter AS quarter,

    country AS country,
    industry AS industry,

    ai_adoption_rate AS ai_adoption_rate,
    years_using_ai AS years_using_ai,
    num_ai_tools_used AS num_ai_tools_used,
    ai_projects_active AS ai_projects_active,

    ai_training_hours AS ai_training_hours,
    ai_maturity_score AS ai_maturity_score,
    ai_failure_rate AS ai_failure_rate,
    ai_risk_management_score AS ai_risk_management_score,

    productivity_change_percent AS productivity_change_percent,
    revenue_growth_percent AS revenue_growth_percent,
    cost_reduction_percent AS cost_reduction_percent,
    innovation_score AS innovation_score,
    customer_satisfaction AS customer_satisfaction,

    remote_work_percentage AS remote_work_percentage,
    employee_satisfaction_score AS employee_satisfaction_score,
    task_automation_rate AS task_automation_rate,
    time_saved_per_week AS time_saved_per_week,
    jobs_displaced AS jobs_displaced,
    jobs_created AS jobs_created,
    reskilled_employees AS reskilled_employees
FROM CONS_PG_ORCL_EVENT_V;

-----
SELECT *
FROM FACT_COMPANY_AI_EVENT_V
FETCH FIRST 20 ROWS ONLY;

--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
                                                --  DIMENSIONAL VIEWS
--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------

-- DIM_TIME_V dimensional view
-- dimensiune temporală pentru modelul OLAP
-- survey_year + quarter
/* Scop: să definească dimensiunea timp pentru analiză:
permite agregări și comparații pe ani și trimestre
Fact view
FACT_COMPANY_AI_EVENT_V */

CREATE OR REPLACE VIEW DIM_TIME_V AS
SELECT DISTINCT
    survey_year AS survey_year,
    quarter AS quarter,

    -- pentru sortare și OLAP
    survey_year || ' ' || quarter AS year_quarter_label,

    -- numeric helper (ex: 20231, 20232 etc.)
    survey_year * 10 +
        CASE
            WHEN UPPER(quarter) = 'Q1' THEN 1
            WHEN UPPER(quarter) = 'Q2' THEN 2
            WHEN UPPER(quarter) = 'Q3' THEN 3
            WHEN UPPER(quarter) = 'Q4' THEN 4
            ELSE 0
        END AS year_quarter_key

FROM FACT_COMPANY_AI_EVENT_V;

-- DIM_COMPANY_V dimensional view
-- dimensiune companie pentru modelul OLAP
-- 1 rând per company_id
/* Scop: să definească dimensiunea companie pentru analiză:
permite agregări și filtrări pe companie și atributele descriptive ale companiei
Consolidation views
CONS_PG_ORCL_EVENT_V */

CREATE OR REPLACE VIEW DIM_COMPANY_V AS
SELECT
    company_id AS company_id,
    MIN(country) AS country,
    MIN(industry) AS industry,
    MIN(company_size) AS company_size,
    MIN(num_employees) AS num_employees,
    MIN(annual_revenue_usd_millions) AS annual_revenue_usd_millions
FROM CONS_PG_ORCL_EVENT_V
GROUP BY company_id;

SELECT *
FROM DIM_COMPANY_V
FETCH FIRST 20 ROWS ONLY;

-- DIM_COUNTRY_V dimensional view
-- dimensiune țară pentru modelul OLAP
-- 1 rând per country
/* Scop: să definească dimensiunea țară pentru analiză:
permite agregări și comparații pe țări
Fact view
FACT_COMPANY_AI_EVENT_V */

CREATE OR REPLACE VIEW DIM_COUNTRY_V AS
SELECT DISTINCT
    country AS country
FROM FACT_COMPANY_AI_EVENT_V;

SELECT *
FROM DIM_COUNTRY_V
ORDER BY country;

-- DIM_INDUSTRY_V dimensional view
-- dimensiune industrie pentru modelul OLAP
-- 1 rând per industry
/* Scop: să definească dimensiunea industrie pentru analiză:
permite agregări și comparații pe industrii
Fact view
FACT_COMPANY_AI_EVENT_V */

CREATE OR REPLACE VIEW DIM_INDUSTRY_V AS
SELECT DISTINCT
    industry AS industry
FROM FACT_COMPANY_AI_EVENT_V;

SELECT *
FROM DIM_INDUSTRY_V
ORDER BY industry;

--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
                                                --  OLAP views
--------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------
--Care sunt top țările după adopția AI?
--Cum se compară fiecare țară cu media globală?
--Care este trendul în timp fără să pierzi granularitatea?
--Care este poziția unei țări în clasament?

-- OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V analytical view
-- analiză pe țară și timp folosind funcții analitice
/* Scop: să analizeze:
ranking-ul țărilor după adopția AI,
comparația cu media globală,
evoluția în timp fără agregare destructivă
Fact view
FACT_COMPANY_AI_EVENT_V */

CREATE OR REPLACE VIEW OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V AS
WITH BASE_WINDOW AS (
    SELECT
        country,
        survey_year,
        quarter,
        ai_adoption_rate,
        ai_maturity_score,
        jobs_created,
        jobs_displaced,

        ROUND(AVG(ai_adoption_rate) OVER (PARTITION BY country), 2) AS avg_country_ai_adoption,
        ROUND(AVG(ai_adoption_rate) OVER (), 2) AS avg_global_ai_adoption,

        ROUND(
            AVG(ai_adoption_rate) OVER (PARTITION BY country)
            - AVG(ai_adoption_rate) OVER (),
            2
        ) AS diff_vs_global,

        SUM(jobs_created) OVER (PARTITION BY country) AS total_jobs_created_country,

        SUM(jobs_created) OVER (
            PARTITION BY country
            ORDER BY survey_year, quarter
            ROWS UNBOUNDED PRECEDING
        ) AS cumulative_jobs_created
    FROM FACT_COMPANY_AI_EVENT_V
)
SELECT
    country,
    survey_year,
    quarter,
    ai_adoption_rate,
    ai_maturity_score,
    jobs_created,
    jobs_displaced,
    avg_country_ai_adoption,
    avg_global_ai_adoption,
    diff_vs_global,
    RANK() OVER (ORDER BY avg_country_ai_adoption DESC) AS country_rank,
    total_jobs_created_country,
    cumulative_jobs_created
FROM BASE_WINDOW;

-- 
SELECT *
FROM OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V
FETCH FIRST 20 ROWS ONLY;


--Care este nivelul adopției AI pe fiecare combinație țară-industrie?
--Ce țări au cele mai bune scoruri agregate, indiferent de industrie?
--Ce industrii performează mai bine, indiferent de țară?
--Care este totalul general?
-- OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V analytical OLAP view
-- analiză multidimensională cu operatorul CUBE
/* Scop: să analizeze:
indicatorii AI și workforce pe combinații de țară și industrie,
subtotalurile pe țară,
subtotalurile pe industrie,
totalul general
Fact view
FACT_COMPANY_AI_EVENT_V */

CREATE OR REPLACE VIEW OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V AS
SELECT
    CASE
        WHEN GROUPING(country) = 1 THEN '{ALL_COUNTRIES}'
        ELSE country
    END AS country,

    CASE
        WHEN GROUPING(industry) = 1 THEN
            CASE
                WHEN GROUPING(country) = 1 THEN ' '
                ELSE 'subtotal country ' || country
            END
        ELSE industry
    END AS industry,

    ROUND(AVG(ai_adoption_rate), 2) AS avg_ai_adoption_rate,
    ROUND(AVG(ai_maturity_score), 3) AS avg_ai_maturity_score,
    ROUND(AVG(ai_failure_rate), 3) AS avg_ai_failure_rate,
    ROUND(AVG(ai_risk_management_score), 3) AS avg_ai_risk_management_score,

    SUM(ai_projects_active) AS total_ai_projects_active,
    SUM(jobs_created) AS total_jobs_created,
    SUM(jobs_displaced) AS total_jobs_displaced

FROM FACT_COMPANY_AI_EVENT_V
GROUP BY CUBE(country, industry)
ORDER BY country, industry;

SELECT *
FROM OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V
FETCH FIRST 30 ROWS ONLY;

---Cum diferă adopția AI și impactul în business/workforce în funcție de mărimea companiei și de țară?
---În ce țări companiile mari adoptă AI mai mult decât cele mici?
---Ce categorie de dimensiune de companie are cele mai bune rezultate de business?
---Cum se compară jobs_created și jobs_displaced între clasele de mărime?

-- OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V analytical OLAP view
-- analiză multidimensională cu operatorul GROUPING SETS
/* Scop: să analizeze:
indicatorii AI și business/workforce pe combinații de țară și dimensiune companie,
subtotalurile pe țară,
subtotalurile pe dimensiune companie,
totalul general
Fact view
FACT_COMPANY_AI_EVENT_V
Dimensional view
DIM_COMPANY_V */

CREATE OR REPLACE VIEW OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V AS
SELECT
    CASE
        WHEN GROUPING(d.country) = 1 THEN '{ALL_COUNTRIES}'
        ELSE d.country
    END AS country,

    CASE
        WHEN GROUPING(d.company_size) = 1 THEN
            CASE
                WHEN GROUPING(d.country) = 1 THEN '{ALL_COMPANY_SIZES}'
                ELSE 'subtotal country ' || d.country
            END
        ELSE d.company_size
    END AS company_size,

    ROUND(AVG(f.ai_adoption_rate), 2) AS avg_ai_adoption_rate,
    ROUND(AVG(f.ai_maturity_score), 3) AS avg_ai_maturity_score,
    ROUND(AVG(f.productivity_change_percent), 2) AS avg_productivity_change_percent,

    SUM(f.jobs_created) AS total_jobs_created,
    SUM(f.jobs_displaced) AS total_jobs_displaced,
    SUM(f.reskilled_employees) AS total_reskilled_employees

FROM FACT_COMPANY_AI_EVENT_V f
INNER JOIN DIM_COMPANY_V d
    ON f.company_id = d.company_id

GROUP BY GROUPING SETS (
    (d.country, d.company_size),
    (d.country),
    (d.company_size),
    ()
)
ORDER BY country, company_size;

SELECT *
FROM OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V
FETCH FIRST 30 ROWS ONLY;

--Cum se compară valorile reale consolidate cu valorile raportate în Excel?
--Cum se comportă țările în raport cu contextul lor macro și digital?
--Există diferențe între adopția AI observată și benchmark-ul/mediul de țară?
--Ce țări au diferențe mari între valorile consolidate și cele raportate în Excel?
--Există țări cu adopție AI mare, dar cu maturitate digitală redusă?
--În ce regiuni apar cele mai mari deviații?
--Cum arată variațiile pe perioade?

-- OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V analytical OLAP view
-- analiză multi-source pe țară și perioadă folosind view-ul global de consolidare
/* Scop: să analizeze:
diferențele dintre valorile consolidate și cele raportate în Excel,
contextul macro și AI ecosystem la nivel de țară,
variațiile pe perioade și regiuni
Consolidation view
CONS_GLOBAL_COUNTRY_TIME_V */

CREATE OR REPLACE VIEW OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V AS
SELECT
    country AS country,
    survey_year AS survey_year,
    quarter AS quarter,
    NVL(region, 'UNKNOWN_REGION') AS region,

    ROUND(AVG(event_avg_ai_adoption_rate), 2) AS avg_event_ai_adoption_rate,
    ROUND(AVG(xls_avg_ai_adoption_rate), 2) AS avg_xls_ai_adoption_rate,
    ROUND(AVG(diff_ai_adoption_rate), 2) AS avg_diff_ai_adoption_rate,

    ROUND(AVG(event_avg_ai_maturity_score), 3) AS avg_event_ai_maturity_score,
    ROUND(AVG(xls_avg_ai_maturity_score), 3) AS avg_xls_ai_maturity_score,
    ROUND(AVG(diff_ai_maturity_score), 3) AS avg_diff_ai_maturity_score,

    ROUND(AVG(event_avg_productivity_change_percent), 2) AS avg_event_productivity_change_percent,
    ROUND(AVG(xls_avg_productivity_change_percent), 2) AS avg_xls_productivity_change_percent,
    ROUND(AVG(diff_productivity_change_percent), 2) AS avg_diff_productivity_change_percent,

    ROUND(AVG(gdp_per_capita), 2) AS avg_gdp_per_capita,
    ROUND(AVG(internet_penetration), 2) AS avg_internet_penetration,
    ROUND(AVG(digital_maturity_index), 3) AS avg_digital_maturity_index,
    ROUND(AVG(ai_researchers_per_million), 2) AS avg_ai_researchers_per_million,

    SUM(event_total_jobs_created) AS total_event_jobs_created,
    SUM(event_total_jobs_displaced) AS total_event_jobs_displaced,
    SUM(xls_total_jobs_created) AS total_xls_jobs_created,
    SUM(xls_total_jobs_displaced) AS total_xls_jobs_displaced

FROM CONS_GLOBAL_COUNTRY_TIME_V
GROUP BY
    country,
    survey_year,
    quarter,
    NVL(region, 'UNKNOWN_REGION')
ORDER BY
    country,
    survey_year,
    quarter;
    
    SELECT *
FROM OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V
FETCH FIRST 30 ROWS ONLY;