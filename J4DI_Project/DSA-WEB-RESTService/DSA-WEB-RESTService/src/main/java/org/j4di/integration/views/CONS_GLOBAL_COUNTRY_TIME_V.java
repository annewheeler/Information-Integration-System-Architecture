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
@IdClass(CONS_GLOBAL_COUNTRY_TIME_V_Id.class)
@Table(name = "CONS_GLOBAL_COUNTRY_TIME_V")
public class CONS_GLOBAL_COUNTRY_TIME_V {

    @Id
    private String country;

    @Id
    private Long survey_year;

    @Id
    private String quarter;

    private Long xls_num_companies;
    private Double xls_avg_ai_adoption_rate;
    private Double xls_avg_ai_maturity_score;
    private Double xls_avg_productivity_change_percent;
    private Long xls_total_jobs_displaced;
    private Long xls_total_jobs_created;

    private Long event_num_companies;
    private Double event_avg_ai_adoption_rate;
    private Double event_avg_ai_maturity_score;
    private Double event_avg_productivity_change_percent;
    private Long event_total_jobs_displaced;
    private Long event_total_jobs_created;

    private Long diff_num_companies;
    private Double diff_ai_adoption_rate;
    private Double diff_ai_maturity_score;
    private Double diff_productivity_change_percent;
    private Long diff_total_jobs_displaced;
    private Long diff_total_jobs_created;

    private String region;
    private Double gdp_per_capita;
    private Double internet_penetration;
    private Double digital_maturity_index;
    private String country_ai_policy;
    private Long ai_patent_filings_2024;
    private Double ai_researchers_per_million;
}