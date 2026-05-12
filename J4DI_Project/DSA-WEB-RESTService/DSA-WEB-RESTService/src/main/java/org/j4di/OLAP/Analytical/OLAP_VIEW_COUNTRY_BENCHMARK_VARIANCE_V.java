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
@IdClass(OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V_Id.class)
@Table(name = "OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V")
public class OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V {

    @Id
    private String country;

    @Id
    private Long survey_year;

    @Id
    private String quarter;

    private String region;
    private Double avg_event_ai_adoption_rate;
    private Double avg_xls_ai_adoption_rate;
    private Double avg_diff_ai_adoption_rate;
    private Double avg_event_ai_maturity_score;
    private Double avg_xls_ai_maturity_score;
    private Double avg_diff_ai_maturity_score;
    private Double avg_event_productivity_change_percent;
    private Double avg_xls_productivity_change_percent;
    private Double avg_diff_productivity_change_percent;
    private Double avg_gdp_per_capita;
    private Double avg_internet_penetration;
    private Double avg_digital_maturity_index;
    private Double avg_ai_researchers_per_million;
    private Long total_event_jobs_created;
    private Long total_event_jobs_displaced;
    private Long total_xls_jobs_created;
    private Long total_xls_jobs_displaced;
}