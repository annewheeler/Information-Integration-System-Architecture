package org.datasource.poi.reporting;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.datasource.poi.XLSXResourceFileDataSourceConnector;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CountryQuarterSummaryViewBuilder {

    private List<CountryQuarterSummaryView> viewList;

    public List<CountryQuarterSummaryView> getViewList() {
        return viewList;
    }

    private XLSXResourceFileDataSourceConnector dataSourceConnector;
    private File xlsxFile;

    public CountryQuarterSummaryViewBuilder(XLSXResourceFileDataSourceConnector dataSourceConnector) throws Exception {
        this.dataSourceConnector = dataSourceConnector;
        this.xlsxFile = dataSourceConnector.getXLSXFile();
    }

    public CountryQuarterSummaryViewBuilder build() throws Exception {
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .headerStart(0)
                .build();

        this.viewList = Poiji.fromExcel(this.xlsxFile, CountryQuarterSummaryView.class, options);

        System.out.println("SELECT CountryQuarterSummaryView: " + this.viewList.size() + " rows processed.");

        return this;
    }
}