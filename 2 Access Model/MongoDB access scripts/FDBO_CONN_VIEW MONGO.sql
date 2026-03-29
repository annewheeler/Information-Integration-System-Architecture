CREATE OR REPLACE FUNCTION get_mongo_data RETURN CLOB IS
  req   UTL_HTTP.req;
  resp  UTL_HTTP.resp;
  data  CLOB;
BEGIN
  req := UTL_HTTP.begin_request(
    'http://admin:secret@localhost:8080/ds_mongo_ai_profiles/country_ai_profiles',
    'GET'
  );

  resp := UTL_HTTP.get_response(req);

  UTL_HTTP.read_text(resp, data);

  UTL_HTTP.end_response(resp);

  RETURN data;
END;
/

SELECT get_mongo_data FROM dual;

CREATE OR REPLACE VIEW MONGO_COUNTRY_AI_V AS
SELECT
    jt.country,
    jt.region,
    jt.gdp_per_capita,
    jt.internet_penetration
FROM dual,
JSON_TABLE(
    get_mongo_data,
    '$[*]'
    COLUMNS (
        country VARCHAR2(50) PATH '$.country',
        region VARCHAR2(50) PATH '$.region',
        gdp_per_capita NUMBER PATH '$.macro_indicators.gdp_per_capita',
        internet_penetration NUMBER PATH '$.macro_indicators.internet_penetration'
    )
) jt;

SELECT * FROM MONGO_COUNTRY_AI_V;

CREATE OR REPLACE VIEW MONGO_COUNTRY_AI_PROFILE_V AS
SELECT
    jt.country,
    jt.region,
    jt.gdp_per_capita,
    jt.internet_penetration,
    jt.digital_maturity_index,
    jt.country_ai_policy,
    jt.ai_patent_filings_2024,
    jt.ai_researchers_per_million
FROM dual,
JSON_TABLE(
    get_mongo_data,
    '$[*]'
    COLUMNS (
        country                    VARCHAR2(100) PATH '$.country',
        region                     VARCHAR2(100) PATH '$.region',
        gdp_per_capita             NUMBER        PATH '$.macro_indicators.gdp_per_capita',
        internet_penetration       NUMBER        PATH '$.macro_indicators.internet_penetration',
        digital_maturity_index     NUMBER        PATH '$.macro_indicators.digital_maturity_index',
        country_ai_policy          VARCHAR2(100) PATH '$.ai_ecosystem.country_ai_policy',
        ai_patent_filings_2024     NUMBER        PATH '$.ai_ecosystem.ai_patent_filings_2024',
        ai_researchers_per_million NUMBER        PATH '$.ai_ecosystem.ai_researchers_per_million'
    )
) jt;


CREATE OR REPLACE VIEW MONGO_COUNTRY_AI_BENCHMARKS_V AS
SELECT
    jt.country,
    jt.region,
    jt.industry,
    jt.avg_ai_adoption_rate,
    jt.avg_ai_maturity_score,
    jt.avg_ai_failure_rate
FROM dual,
JSON_TABLE(
    get_mongo_data,
    '$[*]'
    COLUMNS (
        country VARCHAR2(100) PATH '$.country',
        region  VARCHAR2(100) PATH '$.region',
        NESTED PATH '$.industry_benchmarks[*]'
        COLUMNS (
            industry               VARCHAR2(100) PATH '$.industry',
            avg_ai_adoption_rate   NUMBER        PATH '$.adoption.avg_ai_adoption_rate',
            avg_ai_maturity_score  NUMBER        PATH '$.adoption.avg_ai_maturity_score',
            avg_ai_failure_rate    NUMBER        PATH '$.risk.avg_ai_failure_rate'
        )
    )
) jt;