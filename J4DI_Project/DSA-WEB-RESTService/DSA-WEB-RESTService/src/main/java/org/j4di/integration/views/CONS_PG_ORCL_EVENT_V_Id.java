package org.j4di.integration.views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CONS_PG_ORCL_EVENT_V_Id implements Serializable {

    private Long response_id;
    private String company_id;
    private Long survey_year;
    private String quarter;
}