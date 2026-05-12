package org.j4di.integration.views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CONS_PG_XLS_COUNTRY_TIME_V_Id implements Serializable {

    private String country;
    private Long survey_year;
    private String quarter;
}