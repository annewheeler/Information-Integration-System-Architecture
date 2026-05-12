package org.j4di.access.views.excel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XLS_COUNTRY_QUARTER_SUMMARY_V_Repository
        extends JpaRepository<XLS_COUNTRY_QUARTER_SUMMARY_V, XLS_COUNTRY_QUARTER_SUMMARY_V_Id> {

    @Query("SELECT x FROM XLS_COUNTRY_QUARTER_SUMMARY_V x")
    List<XLS_COUNTRY_QUARTER_SUMMARY_V> get_XLS_COUNTRY_QUARTER_SUMMARY_V();
}