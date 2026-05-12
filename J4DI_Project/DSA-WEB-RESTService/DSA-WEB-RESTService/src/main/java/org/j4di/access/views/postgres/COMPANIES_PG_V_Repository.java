package org.j4di.access.views.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface COMPANIES_PG_V_Repository extends JpaRepository<COMPANIES_PG_V, String> {

    @Query("SELECT c FROM COMPANIES_PG_V c")
    List<COMPANIES_PG_V> get_COMPANIES_PG_V();
}