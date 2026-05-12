package org.j4di.access.views.excel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(XLS_COUNTRY_QUARTER_SUMMARY_V_Id.class)
@Table(name = "XLS_COUNTRY_QUARTER_SUMMARY_V")
public class XLS_COUNTRY_QUARTER_SUMMARY_V {

    @Id
    private String country;

    @Id
    private Long surveyYear;

    @Id
    private String quarter;

    private Double avgAiAdoptionRate;
    private Double avgAiMaturityScore;
    private Double avgProductivityChangePercent;
    private Long numCompanies;
    private Long totalJobsCreated;
    private Long totalJobsDisplaced;
}