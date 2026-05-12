package org.j4di.OLAP.Analytical;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V_Id.class)
@Table(name = "OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V")
public class OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V {

    @Id
    private String country;

    @Id
    private Integer survey_year;

    @Id
    private String quarter;

    private Double ai_adoption_rate;
    private Double ai_maturity_score;
    private Long jobs_created;
    private Long jobs_displaced;
    private Double avg_country_ai_adoption;
    private Double avg_global_ai_adoption;
    private Double diff_vs_global;
    private Integer country_rank;
    private Long total_jobs_created_country;
    private Long cumulative_jobs_created;
}