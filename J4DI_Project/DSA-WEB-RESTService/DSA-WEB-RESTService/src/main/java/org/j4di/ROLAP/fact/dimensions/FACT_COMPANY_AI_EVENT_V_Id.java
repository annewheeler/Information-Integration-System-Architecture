package org.j4di.ROLAP.fact.dimensions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FACT_COMPANY_AI_EVENT_V_Id implements Serializable {
    private String company_id;
    private Long response_id;
    private Integer survey_year;
    private String quarter;
}