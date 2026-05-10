package org.datasource.jdbc.views.adoption;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CompanyAiAdoptionPgView {
    private Integer responseId;
    private String companyId;
    private Integer surveyYear;
    private String quarter;
    private BigDecimal aiAdoptionRate;
    private String aiAdoptionStage;
    private Integer yearsUsingAi;
    private String aiPrimaryTool;
    private Integer numAiToolsUsed;
    private String aiUseCase;
    private Integer aiProjectsActive;
}