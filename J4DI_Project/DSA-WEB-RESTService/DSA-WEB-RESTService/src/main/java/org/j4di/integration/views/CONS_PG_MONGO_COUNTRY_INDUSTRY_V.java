package org.j4di.integration.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Id.class)
@Table(name = "CONS_PG_MONGO_COUNTRY_INDUSTRY_V")
public class CONS_PG_MONGO_COUNTRY_INDUSTRY_V {

    @Id
    private String country;

    @Id
    private String industry;

    private String region;

    private Double mongo_avg_ai_adoption_rate;
    private Double mongo_avg_ai_maturity_score;
    private Double mongo_avg_ai_failure_rate;

    private Double pg_avg_ai_adoption_rate;
    private Double pg_avg_ai_maturity_score;
    private Double pg_avg_ai_failure_rate;

    private Long pg_num_events;

    private Double diff_ai_adoption_rate;
    private Double diff_ai_maturity_score;
    private Double diff_ai_failure_rate;
}