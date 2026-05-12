package org.j4di.access.views.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface COMPANY_AI_GOVERNANCE_PG_V_Repository
        extends JpaRepository<COMPANY_AI_GOVERNANCE_PG_V, COMPANY_AI_GOVERNANCE_PG_V_Id> {

    @Query("SELECT g FROM COMPANY_AI_GOVERNANCE_PG_V g")
    List<COMPANY_AI_GOVERNANCE_PG_V> get_COMPANY_AI_GOVERNANCE_PG_V();
}