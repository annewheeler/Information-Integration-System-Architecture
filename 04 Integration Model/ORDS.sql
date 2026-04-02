SELECT *
FROM OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V
FETCH FIRST 5 ROWS ONLY;â

BEGIN
    ORDS.ENABLE_OBJECT(
        p_enabled        => TRUE,
        p_schema         => 'FDBO',
        p_object         => 'OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V',
        p_object_type    => 'VIEW',
        p_object_alias   => 'OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V',
        p_auto_rest_auth => FALSE
    );
    COMMIT;
END;
/

SELECT *
FROM FACT_COMPANY_AI_EVENT_V
FETCH FIRST 5 ROWS ONLY;

BEGIN
    ORDS.ENABLE_OBJECT(
        p_enabled        => TRUE,
        p_schema         => 'FDBO',
        p_object         => 'FACT_COMPANY_AI_EVENT_V',
        p_object_type    => 'VIEW',
        p_object_alias   => 'FACT_COMPANY_AI_EVENT_V',
        p_auto_rest_auth => FALSE
    );
    COMMIT;
END;
/