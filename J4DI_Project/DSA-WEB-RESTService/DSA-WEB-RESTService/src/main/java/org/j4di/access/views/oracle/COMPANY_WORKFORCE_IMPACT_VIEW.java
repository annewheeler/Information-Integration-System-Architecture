package org.j4di.access.views.oracle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(COMPANY_WORKFORCE_IMPACT_VIEW_Id.class)
@Table(name = "COMPANY_WORKFORCE_IMPACT_VIEW")
public class COMPANY_WORKFORCE_IMPACT_VIEW {

    @Id
    private String companyId;

    @Id
    private Long responseId;

    @Id
    private Long surveyYear;

    @Id
    private String quarter;

    private Double employeeSatisfactionScore;
    private Long jobsCreated;
    private Long jobsDisplaced;
    private Double remoteWorkPercentage;
    private Long reskilledEmployees;
    private Double taskAutomationRate;
    private Double timeSavedPerWeek;
}