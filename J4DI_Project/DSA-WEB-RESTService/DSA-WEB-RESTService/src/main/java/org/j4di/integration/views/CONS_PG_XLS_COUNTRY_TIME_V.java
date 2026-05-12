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
@IdClass(CONS_PG_XLS_COUNTRY_TIME_V_Id.class)
@Table(name = "CONS_PG_XLS_COUNTRY_TIME_V")
public class CONS_PG_XLS_COUNTRY_TIME_V {

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

    private Long pg_num_companies;
    private Double pg_avg_ai_adoption_rate;
    private Double pg_avg_ai_maturity_score;

    private Long diff_num_companies;
    private Double diff_ai_adoption_rate;
    private Double diff_ai_maturity_score;
}