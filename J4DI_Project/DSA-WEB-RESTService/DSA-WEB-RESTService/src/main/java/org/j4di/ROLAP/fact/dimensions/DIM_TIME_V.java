package org.j4di.ROLAP.fact.dimensions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@IdClass(DIM_TIME_V_Id.class)
@Table(name = "DIM_TIME_V")
public class DIM_TIME_V {

    @Id
    private Integer survey_year;

    @Id
    private String quarter;

    private String year_quarter_label;

    private Integer year_quarter_key;
}