package org.j4di.access.views.mongo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "MONGO_COUNTRY_AI_PROFILE_V")
public class MONGO_COUNTRY_AI_PROFILE_V {

    @Id
    private String country;

    private Long aiPatentFilings2024;
    private Double aiResearchersPerMillion;
    private String countryAiPolicy;
    private Double digitalMaturityIndex;
    private Double gdpPerCapita;
    private Double internetPenetration;
    private String region;
}