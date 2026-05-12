package org.datasource.jdbc.views.governance;

import lombok.Value;

/*
    View class pentru tabela PostgreSQL: public.company_ai_governance_pg

    Această clasă definește structura obiectului Java returnat de endpointul REST.
    Fiecare câmp corespunde unei coloane din tabela PostgreSQL.
*/
@Value
public class CompanyAiGovernancePgView {
    private Integer responseId;
    private String companyId;
    private Integer surveyYear;
    private String quarter;
    private Integer aiTrainingHours;
    private Double aiMaturityScore;
    private Double aiFailureRate;
    private Double aiRiskManagementScore;
}