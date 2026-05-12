package org.j4di.access.views.mongo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Repository
        extends JpaRepository<MONGO_COUNTRY_INDUSTRY_BENCHMARK_V, MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Id> {

    @Query("SELECT m FROM MONGO_COUNTRY_INDUSTRY_BENCHMARK_V m")
    List<MONGO_COUNTRY_INDUSTRY_BENCHMARK_V> get_MONGO_COUNTRY_INDUSTRY_BENCHMARK_V();
}