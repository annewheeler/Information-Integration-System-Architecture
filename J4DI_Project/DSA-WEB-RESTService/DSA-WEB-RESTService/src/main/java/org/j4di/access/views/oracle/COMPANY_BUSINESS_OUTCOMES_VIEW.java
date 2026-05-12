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
@IdClass(COMPANY_BUSINESS_OUTCOMES_VIEW_Id.class)
@Table(name = "COMPANY_BUSINESS_OUTCOMES_VIEW")
public class COMPANY_BUSINESS_OUTCOMES_VIEW {

    @Id
    private String companyId;

    @Id
    private Long responseId;

    @Id
    private Long surveyYear;

    @Id
    private String quarter;

    private Double costReductionPercent;
    private Double customerSatisfaction;
    private Long innovationScore;
    private Double productivityChangePercent;
    private Double revenueGrowthPercent;
}