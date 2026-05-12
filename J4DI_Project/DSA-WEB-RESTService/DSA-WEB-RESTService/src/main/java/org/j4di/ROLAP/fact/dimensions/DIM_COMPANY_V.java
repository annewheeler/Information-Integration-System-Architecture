package org.j4di.ROLAP.fact.dimensions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "DIM_COMPANY_V")
public class DIM_COMPANY_V {

    @Id
    private String company_id;

    private String country;
    private String industry;
    private String company_size;
    private Long num_employees;
    private Double annual_revenue_usd_millions;
}