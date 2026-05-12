package org.j4di.access.views.mongo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MONGO_COUNTRY_AI_PROFILE_V_Repository
        extends JpaRepository<MONGO_COUNTRY_AI_PROFILE_V, String> {

    @Query("SELECT m FROM MONGO_COUNTRY_AI_PROFILE_V m")
    List<MONGO_COUNTRY_AI_PROFILE_V> get_MONGO_COUNTRY_AI_PROFILE_V();
}