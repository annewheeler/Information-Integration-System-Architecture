package org.j4di.ROLAP.fact.dimensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FACT_COMPANY_AI_EVENT_V_Repository
        extends JpaRepository<FACT_COMPANY_AI_EVENT_V, FACT_COMPANY_AI_EVENT_V_Id> {

    @Query("SELECT f FROM FACT_COMPANY_AI_EVENT_V f")
    List<FACT_COMPANY_AI_EVENT_V> get_FACT_COMPANY_AI_EVENT_V();
}