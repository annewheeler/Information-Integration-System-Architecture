package org.j4di.OLAP.Analytical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Repository
        extends JpaRepository<OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V, OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Id> {

    @Query("SELECT o FROM OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V o")
    List<OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V> get_OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V();
}