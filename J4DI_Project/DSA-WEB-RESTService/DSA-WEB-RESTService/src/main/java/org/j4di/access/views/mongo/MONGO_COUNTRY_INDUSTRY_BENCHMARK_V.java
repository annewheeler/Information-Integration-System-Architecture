package org.j4di.access.views.mongo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Id.class)
@Table(name = "MONGO_COUNTRY_INDUSTRY_BENCHMARK_V")
public class MONGO_COUNTRY_INDUSTRY_BENCHMARK_V {

    @Id
    private String country;

    @Id
    private String industry;

    private Double avgAiAdoptionRate;
    private Double avgAiFailureRate;
    private Double avgAiMaturityScore;
    private String region;
}