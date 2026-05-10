package org.datasource.jpa.views.companies;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "COMPANIES", schema = "AI_DATA")
@NamedQuery(
        name = "CompanyOrclView.findAll",
        query = "SELECT c FROM CompanyOrclView c"
)
public class CompanyOrclView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "COMPANY_SIZE")
    private String companySize;

    @Column(name = "NUM_EMPLOYEES")
    private Long numEmployees;

    @Column(name = "ANNUAL_REVENUE_USD_MILLIONS")
    private BigDecimal annualRevenueUsdMillions;
}