package org.datasource.jpa.views.workforce;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "COMPANY_WORKFORCE_IMPACT", schema = "AI_DATA")
@NamedQuery(
        name = "CompanyWorkforceImpactOrclView.findAll",
        query = "SELECT w FROM CompanyWorkforceImpactOrclView w"
)
public class CompanyWorkforceImpactOrclView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RESPONSE_ID")
    private Long responseId;

    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "SURVEY_YEAR")
    private Integer surveyYear;

    @Column(name = "QUARTER")
    private String quarter;

    @Column(name = "REMOTE_WORK_PERCENTAGE")
    private BigDecimal remoteWorkPercentage;

    @Column(name = "EMPLOYEE_SATISFACTION_SCORE")
    private BigDecimal employeeSatisfactionScore;

    @Column(name = "TASK_AUTOMATION_RATE")
    private BigDecimal taskAutomationRate;

    @Column(name = "TIME_SAVED_PER_WEEK")
    private BigDecimal timeSavedPerWeek;

    @Column(name = "JOBS_DISPLACED")
    private Long jobsDisplaced;

    @Column(name = "JOBS_CREATED")
    private Long jobsCreated;

    @Column(name = "RESKILLED_EMPLOYEES")
    private Long reskilledEmployees;
}