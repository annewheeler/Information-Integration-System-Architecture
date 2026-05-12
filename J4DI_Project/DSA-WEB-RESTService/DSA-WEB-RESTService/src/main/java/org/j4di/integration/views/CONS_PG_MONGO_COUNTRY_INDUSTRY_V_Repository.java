package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Repository
        extends JpaRepository<CONS_PG_MONGO_COUNTRY_INDUSTRY_V, CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Id> {

    @Query("SELECT c FROM CONS_PG_MONGO_COUNTRY_INDUSTRY_V c")
    List<CONS_PG_MONGO_COUNTRY_INDUSTRY_V> get_CONS_PG_MONGO_COUNTRY_INDUSTRY_V();
}