package org.j4di.access.views.oracle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface COMPANY_BUSINESS_OUTCOMES_VIEW_Repository
        extends JpaRepository<COMPANY_BUSINESS_OUTCOMES_VIEW, COMPANY_BUSINESS_OUTCOMES_VIEW_Id> {

    @Query("SELECT b FROM COMPANY_BUSINESS_OUTCOMES_VIEW b")
    List<COMPANY_BUSINESS_OUTCOMES_VIEW> get_COMPANY_BUSINESS_OUTCOMES_VIEW();
}