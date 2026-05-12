package org.j4di.ROLAP.fact.dimensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DIM_TIME_V_Repository
        extends JpaRepository<DIM_TIME_V, DIM_TIME_V_Id> {

    @Query("SELECT d FROM DIM_TIME_V d")
    List<DIM_TIME_V> get_DIM_TIME_V();
}