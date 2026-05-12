package org.datasource.jdbc.views.companies;

import lombok.Value;

@Value
public class CompaniesPgView {
    private String companyId;
    private String industry;
    private String country;
    private String companySize;
}