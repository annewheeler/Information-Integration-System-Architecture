package org.j4di.ROLAP.fact.dimensions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DIM_TIME_V_Id implements Serializable {

    private Integer survey_year;
    private String quarter;
}