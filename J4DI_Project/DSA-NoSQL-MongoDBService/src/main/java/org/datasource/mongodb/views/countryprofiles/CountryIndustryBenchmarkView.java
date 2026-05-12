package org.datasource.mongodb.views.countryprofiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CountryIndustryBenchmarkView {

    private String country;
    private String region;
    private String industry;

    private Double avgAiAdoptionRate;
    private Double avgAiMaturityScore;
    private Double avgAiFailureRate;
}