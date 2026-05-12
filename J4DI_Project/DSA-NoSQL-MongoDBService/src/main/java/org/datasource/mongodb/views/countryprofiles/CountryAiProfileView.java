package org.datasource.mongodb.views.countryprofiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CountryAiProfileView {

    private String country;
    private String region;

    private Double gdpPerCapita;
    private Double internetPenetration;
    private Double digitalMaturityIndex;

    private String countryAiPolicy;
    private Integer aiPatentFilings2024;
    private Double aiResearchersPerMillion;
}