package org.j4di.access.views.excel;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class XLS_COUNTRY_QUARTER_SUMMARY_V_Id implements Serializable {

    private String country;
    private Long surveyYear;
    private String quarter;
}