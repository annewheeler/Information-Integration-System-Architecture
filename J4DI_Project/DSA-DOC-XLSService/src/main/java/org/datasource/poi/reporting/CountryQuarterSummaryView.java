package org.datasource.poi.reporting;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ExcelSheet("country_quarter_summary")
public class CountryQuarterSummaryView {

    @ExcelCell(0)
    private String country;

    @ExcelCell(1)
    private Integer surveyYear;

    @ExcelCell(2)
    private String quarter;

    @ExcelCell(3)
    private Integer numCompanies;

    @ExcelCell(4)
    private Double avgAiAdoptionRate;

    @ExcelCell(5)
    private Double avgAiMaturityScore;

    @ExcelCell(6)
    private Double avgProductivityChangePercent;

    @ExcelCell(7)
    private Integer totalJobsDisplaced;

    @ExcelCell(8)
    private Integer totalJobsCreated;
}