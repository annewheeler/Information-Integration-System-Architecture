package org.datasource.jpa.views.businessoutcomes;

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
@Table(name = "COMPANY_BUSINESS_OUTCOMES", schema = "AI_DATA")
@NamedQuery(
        name = "CompanyBusinessOutcomesOrclView.findAll",
        query = "SELECT b FROM CompanyBusinessOutcomesOrclView b"
)
public class CompanyBusinessOutcomesOrclView implements Serializable {
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

    @Column(name = "PRODUCTIVITY_CHANGE_PERCENT")
    private BigDecimal productivityChangePercent;

    @Column(name = "REVENUE_GROWTH_PERCENT")
    private BigDecimal revenueGrowthPercent;

    @Column(name = "COST_REDUCTION_PERCENT")
    private BigDecimal costReductionPercent;

    @Column(name = "INNOVATION_SCORE")
    private BigDecimal innovationScore;

    @Column(name = "CUSTOMER_SATISFACTION")
    private BigDecimal customerSatisfaction;
}