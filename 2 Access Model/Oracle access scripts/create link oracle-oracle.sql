ROLLBACK;
ALTER SESSION CLOSE DATABASE LINK ai_data_db;

DROP DATABASE LINK ai_data_db;

CREATE DATABASE LINK ai_data_db
   CONNECT TO AI_DATA IDENTIFIED BY oracle
   USING '//localhost:1521/XEPDB1';

SELECT db_link, username, host
FROM user_db_links;

SELECT *
FROM user_tables@ai_data_db;

SELECT *
FROM COMPANIES@ai_data_db
WHERE ROWNUM <= 5;

