package org.j4di.access.views.postgres;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class COMPANY_AI_ADOPTION_PG_V_Id implements Serializable {

    private String companyId;
    private Long responseId;
    private Long surveyYear;
    private String quarter;
}