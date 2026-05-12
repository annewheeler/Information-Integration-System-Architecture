package org.j4di.ROLAP.fact.dimensions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "DIM_COUNTRY_V")
public class DIM_COUNTRY_V {

    @Id
    private String country;
}