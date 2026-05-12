package org.j4di.OLAP.Analytical;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Id implements Serializable {

    private String country;
    private String industry;
}