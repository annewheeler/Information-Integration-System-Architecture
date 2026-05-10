package org.datasource.jdbc.views.governance;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CompanyAiGovernancePgView {
    private Integer responseId;
    private String companyId;
    private Integer surveyYear;
    private String quarter;
    private Integer aiTrainingHours;
    private BigDecimal aiMaturityScore;
    private BigDecimal aiFailureRate;
    private BigDecimal aiRiskManagementScore;
}