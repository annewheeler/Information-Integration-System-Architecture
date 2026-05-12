package org.j4di.ROLAP.fact.dimensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DIM_INDUSTRY_V_Repository
        extends JpaRepository<DIM_INDUSTRY_V, String> {

    @Query("SELECT d FROM DIM_INDUSTRY_V d")
    List<DIM_INDUSTRY_V> get_DIM_INDUSTRY_V();
}