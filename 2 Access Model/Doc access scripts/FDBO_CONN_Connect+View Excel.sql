
SELECT directory_name, directory_path
FROM all_directories
WHERE directory_name = 'EXT_FILE_DS';

SELECT DBMS_LOB.getlength(
         ExcelTable.getFile('EXT_FILE_DS', 'ai_reporting_local.xlsx')
       ) AS file_size
FROM dual;

SELECT *
FROM TABLE(
    ExcelTable.getSheets(
        ExcelTable.getFile('EXT_FILE_DS', 'ai_reporting_local.xlsx')
    )
);

SELECT *
FROM TABLE(
    ExcelTable.getSheets(
        ExcelTable.getFile('EXT_FILE_DS', 'test_simple.xlsx')
    )
);

SELECT name, type, line, position, text
FROM user_errors
WHERE UPPER(name) LIKE 'EXCELTABLE%'
   OR UPPER(name) LIKE 'XUTL_%'
ORDER BY name, sequence;

SELECT *
FROM TABLE(
    ExcelTable.getRows(
        ExcelTable.getFile('EXT_FILE_DS', 'ai_reporting_local.xlsx'),
        'country_quarter_summary',
        '"country" VARCHAR2(100),
         "survey_year" NUMBER,
         "quarter" VARCHAR2(10),
         "num_companies" NUMBER,
         "avg_ai_adoption_rate" NUMBER,
         "avg_ai_maturity_score" NUMBER,
         "avg_productivity_change_percent" NUMBER,
         "total_jobs_displaced" NUMBER,
         "total_jobs_created" NUMBER',
        'A2:I481'
    )
);

CREATE OR REPLACE VIEW XLS_COUNTRY_QUARTER_SUMMARY_V AS
SELECT *
FROM TABLE(
    ExcelTable.getRows(
        ExcelTable.getFile('EXT_FILE_DS', 'ai_reporting_local.xlsx'),
        'country_quarter_summary',
        '"country" VARCHAR2(100),
         "survey_year" NUMBER,
         "quarter" VARCHAR2(10),
         "num_companies" NUMBER,
         "avg_ai_adoption_rate" NUMBER,
         "avg_ai_maturity_score" NUMBER,
         "avg_productivity_change_percent" NUMBER,
         "total_jobs_displaced" NUMBER,
         "total_jobs_created" NUMBER',
        'A2:I481'
    )
);

SELECT *
FROM XLS_COUNTRY_QUARTER_SUMMARY_V
WHERE ROWNUM <= 10;

SELECT COUNT(*) 
FROM XLS_COUNTRY_QUARTER_SUMMARY_V;