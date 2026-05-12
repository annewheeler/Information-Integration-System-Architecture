package org.j4di.access.views.oracle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface COMPANY_WORKFORCE_IMPACT_VIEW_Repository
        extends JpaRepository<COMPANY_WORKFORCE_IMPACT_VIEW, COMPANY_WORKFORCE_IMPACT_VIEW_Id> {

    @Query("SELECT w FROM COMPANY_WORKFORCE_IMPACT_VIEW w")
    List<COMPANY_WORKFORCE_IMPACT_VIEW> get_COMPANY_WORKFORCE_IMPACT_VIEW();
}