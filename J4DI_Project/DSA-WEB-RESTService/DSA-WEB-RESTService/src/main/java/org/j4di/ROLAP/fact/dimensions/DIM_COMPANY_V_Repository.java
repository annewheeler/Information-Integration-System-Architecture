package org.j4di.ROLAP.fact.dimensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DIM_COMPANY_V_Repository
        extends JpaRepository<DIM_COMPANY_V, String> {

    @Query("SELECT d FROM DIM_COMPANY_V d")
    List<DIM_COMPANY_V> get_DIM_COMPANY_V();
}