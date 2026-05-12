package org.j4di.ROLAP.fact.dimensions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(FACT_COMPANY_AI_EVENT_V_Id.class)
@Table(name = "FACT_COMPANY_AI_EVENT_V")
public class FACT_COMPANY_AI_EVENT_V {

    @Id
    private String company_id;

    @Id
    private Long response_id;

    @Id
    private Integer survey_year;

    @Id
    private String quarter;

    private String country;
    private String industry;
    private Double ai_adoption_rate;
    private Long years_using_ai;
    private Long num_ai_tools_used;
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