--------------------------------------------------------------------------------
--- 6. OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V
--------------------------------------------------------------------------------

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

SELECT COUNT(*)
FROM OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V;

SELECT *
FROM OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V
LIMIT 10;

--------------------------------------------------------------------------------
--- 7. OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V
--------------------------------------------------------------------------------

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

SELECT COUNT(*)
FROM OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V;

SELECT *
FROM OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V
LIMIT 20;

--------------------------------------------------------------------------------
--- 8. OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V
--------------------------------------------------------------------------------

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

SELECT COUNT(*)
FROM OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V;

SELECT *
FROM OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V
LIMIT 20;

--------------------------------------------------------------------------------
--- 9. OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V AS
SELECT
    country,
    survey_year,
    quarter,
    COALESCE(region, 'UNKNOWN_REGION') AS region,

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
    COALESCE(region, 'UNKNOWN_REGION')

ORDER BY
    country,
    survey_year,
    quarter;

SELECT COUNT(*)
FROM OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V;

SELECT *
FROM OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V
LIMIT 20;