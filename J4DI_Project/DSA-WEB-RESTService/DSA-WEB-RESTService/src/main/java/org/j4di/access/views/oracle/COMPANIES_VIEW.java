package org.j4di.access.views.oracle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "COMPANIES_VIEW")
public class COMPANIES_VIEW {

    @Id
    private String companyId;

    private Double annualRevenueUsdMillions;
    private String companySize;
    private String country;
    private Long numEmployees;
}