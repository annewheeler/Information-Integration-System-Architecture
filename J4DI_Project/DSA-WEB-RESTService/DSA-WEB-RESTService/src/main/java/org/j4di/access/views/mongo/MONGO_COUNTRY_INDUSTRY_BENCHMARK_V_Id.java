package org.j4di.access.views.mongo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Id implements Serializable {

    private String country;
    private String industry;
}