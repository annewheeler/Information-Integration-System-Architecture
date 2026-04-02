--access schema pe web

BEGIN
    ORDS.ENABLE_SCHEMA(
        p_enabled             => TRUE,
        p_schema              => 'FDBO',
        p_url_mapping_type    => 'BASE_PATH',
        p_url_mapping_pattern => 'fdbo',
        p_auto_rest_auth      => FALSE
    );
    COMMIT;
END;
/


--publicare view ca REST (AutoREST)
BEGIN
    ORDS.ENABLE_OBJECT(
        p_enabled        => TRUE,
        p_schema         => 'FDBO',
        p_object         => 'CONS_PG_ORCL_EVENT_V',
        p_object_type    => 'VIEW',
        p_object_alias   => 'CONS_PG_ORCL_EVENT_V',
        p_auto_rest_auth => FALSE
    );
    COMMIT;
END;
/

--REST MODULE

BEGIN
    -- delete module if exists (safe reset)
    ORDS.DELETE_MODULE(
        p_module_name => 'fdbo.api'
    );
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/
BEGIN
    -- create module
    ORDS.DEFINE_MODULE(
        p_module_name    => 'fdbo.api',
        p_base_path      => '/olap/',
        p_items_per_page => 25,
        p_status         => 'PUBLISHED'
    );

    -- create URL: /olap/events
    ORDS.DEFINE_TEMPLATE(
        p_module_name => 'fdbo.api',
        p_pattern     => 'events'
    );

    -- define GET handler
    ORDS.DEFINE_HANDLER(
        p_module_name    => 'fdbo.api',
        p_pattern        => 'events',
        p_method         => 'GET',
        p_source_type    => 'json/collection',
        p_items_per_page => 25,
        p_source         => q'[
            SELECT *
            FROM CONS_PG_ORCL_EVENT_V
        ]'
    );

    COMMIT;
END;
/

-- olap analytical vie
BEGIN
    ORDS.ENABLE_OBJECT(
        p_enabled        => TRUE,
        p_schema         => 'FDBO',
        p_object         => 'OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V',
        p_object_type    => 'VIEW',
        p_object_alias   => 'OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V',
        p_auto_rest_auth => FALSE
    );
    COMMIT;
END;
/