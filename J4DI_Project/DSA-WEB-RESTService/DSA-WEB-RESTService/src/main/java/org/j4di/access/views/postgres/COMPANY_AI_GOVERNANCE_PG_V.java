package org.j4di.access.views.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(COMPANY_AI_GOVERNANCE_PG_V_Id.class)
@Table(name = "COMPANY_AI_GOVERNANCE_PG_V")
public class COMPANY_AI_GOVERNANCE_PG_V {

    @Id
    private String companyId;

    @Id
    private Long responseId;

    @Id
    private Long surveyYear;

    @Id
    private String quarter;

    private Double aiFailureRate;
    private Double aiMaturityScore;
    private Double aiRiskManagementScore;
    private Long aiTrainingHours;
}