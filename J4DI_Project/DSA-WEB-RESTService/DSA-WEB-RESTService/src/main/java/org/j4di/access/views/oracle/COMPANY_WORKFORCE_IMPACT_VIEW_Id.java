package org.j4di.access.views.oracle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class COMPANY_WORKFORCE_IMPACT_VIEW_Id implements Serializable {

    private String companyId;
    private Long responseId;
    private Long surveyYear;
    private String quarter;
}