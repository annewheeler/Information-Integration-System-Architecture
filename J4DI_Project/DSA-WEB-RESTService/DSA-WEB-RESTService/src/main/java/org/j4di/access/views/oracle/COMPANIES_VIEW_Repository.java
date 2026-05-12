package org.j4di.access.views.oracle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface COMPANIES_VIEW_Repository extends JpaRepository<COMPANIES_VIEW, String> {

    @Query("SELECT c FROM COMPANIES_VIEW c")
    List<COMPANIES_VIEW> get_COMPANIES_VIEW();
}