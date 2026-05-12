package org.j4di.integration.views;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Id implements Serializable {

    private String country;
    private String industry;
}