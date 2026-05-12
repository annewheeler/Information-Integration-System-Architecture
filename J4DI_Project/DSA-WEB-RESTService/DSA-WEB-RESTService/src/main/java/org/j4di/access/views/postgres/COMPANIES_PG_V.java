package org.j4di.access.views.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "COMPANIES_PG_V")
public class COMPANIES_PG_V {

    @Id
    private String companyId;

    private String companySize;
    private String country;
    private String industry;
}