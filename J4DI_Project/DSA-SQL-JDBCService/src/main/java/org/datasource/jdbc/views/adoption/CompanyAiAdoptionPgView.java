package org.datasource.jdbc.views.adoption;

import lombok.Value;

/*
    View class pentru tabela PostgreSQL: public.company_ai_adoption_pg

    Această clasă definește structura obiectului Java care va fi returnat
    de endpointul REST. Fiecare câmp corespunde unei coloane din tabela PostgreSQL.
*/
@Value
public class CompanyAiAdoptionPgView {
    private Integer responseId;
    private String companyId;
    private Integer surveyYear;
    private String quarter;
    private Double aiAdoptionRate;
    private String aiAdoptionStage;
    private Integer yearsUsingAi;
    private String aiPrimaryTool;
    private Integer numAiToolsUsed;
    private String aiUseCase;
    private Integer aiProjectsActive;
}