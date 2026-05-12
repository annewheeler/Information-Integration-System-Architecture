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
@IdClass(OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Id.class)
@Table(name = "OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V")
public class OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V {

    @Id
    private String country;

    @Id
    private String company_size;

    private Double avg_ai_adoption_rate;
    private Double avg_ai_maturity_score;
    private Double avg_productivity_change_percent;
    private Long total_jobs_created;
    private Long total_jobs_displaced;
    private Long total_reskilled_employees;
}