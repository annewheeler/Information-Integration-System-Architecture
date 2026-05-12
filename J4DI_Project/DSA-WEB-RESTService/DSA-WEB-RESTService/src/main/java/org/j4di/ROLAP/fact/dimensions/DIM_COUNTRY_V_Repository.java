package org.j4di.ROLAP.fact.dimensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DIM_COUNTRY_V_Repository
        extends JpaRepository<DIM_COUNTRY_V, String> {

    @Query("SELECT d FROM DIM_COUNTRY_V d")
    List<DIM_COUNTRY_V> get_DIM_COUNTRY_V();
}