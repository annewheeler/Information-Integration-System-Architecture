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
@IdClass(CONS_PG_ORCL_EVENT_V_Id.class)
@Table(name = "CONS_PG_ORCL_EVENT_V")
public class CONS_PG_ORCL_EVENT_V {

    @Id
    private Long response_id;

    @Id
    private String company_id;

    @Id
    private Long survey_year;

    @Id
    private String quarter;

    private String industry;
    private String country;
    private String company_size;
    private Long num_employees;
    private Double annual_revenue_usd_millions;
    private Double ai_adoption_rate;
    private String ai_adoption_stage;
    private Long years_using_ai;
    private String ai_primary_tool;
    private Long num_ai_tools_used;
    private String ai_use_case;
    private Long ai_projects_active;
    private Long ai_training_hours;
    private Double ai_maturity_score;
    private Double ai_failure_rate;
    private Double ai_risk_management_score;
    private Double productivity_change_percent;
    private Double revenue_growth_percent;
    private Double cost_reduction_percent;
    private Long innovation_score;
    private Double customer_satisfaction;
    private Double remote_work_percentage;
    private Double employee_satisfaction_score;
    private Double task_automation_rate;
    private Double time_saved_per_week;
    private Long jobs_displaced;
    private Long jobs_created;
    private Long reskilled_employees;
}