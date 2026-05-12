package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Repository
        extends JpaRepository<CONS_ORCL_PG_XLS_COUNTRY_TIME_V, CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Id> {

    @Query("SELECT c FROM CONS_ORCL_PG_XLS_COUNTRY_TIME_V c")
    List<CONS_ORCL_PG_XLS_COUNTRY_TIME_V> get_CONS_ORCL_PG_XLS_COUNTRY_TIME_V();
}