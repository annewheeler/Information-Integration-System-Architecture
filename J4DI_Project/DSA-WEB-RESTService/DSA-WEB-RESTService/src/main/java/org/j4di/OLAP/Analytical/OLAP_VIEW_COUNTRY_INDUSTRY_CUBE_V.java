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
@IdClass(OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Id.class)
@Table(name = "OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V")
public class OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V {

    @Id
    private String country;

    @Id
    private String industry;

    private Double avg_ai_adoption_rate;
    private Double avg_ai_maturity_score;
    private Double avg_ai_failure_rate;
    private Double avg_ai_risk_management_score;
    private Long total_ai_projects_active;
    private Long total_jobs_created;
    private Long total_jobs_displaced;
}