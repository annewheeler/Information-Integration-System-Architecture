package org.datasource;

import org.datasource.poi.reporting.CountryQuarterSummaryView;
import org.datasource.poi.reporting.CountryQuarterSummaryViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
/*
    REST Service URL - Excel AI Reporting source
    Ping:
    http://localhost:8094/DSA-DOC-XLSService/rest/reporting/ping

    Excel source view:
    http://localhost:8094/DSA-DOC-XLSService/rest/reporting/CountryQuarterSummaryView
*/
@RestController
@RequestMapping("/reporting")
public class RESTViewServiceXLS {

	private static Logger logger = Logger.getLogger(RESTViewServiceXLS.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> REST XLS Data Source is Up!");
		return "PING response from DSA-DOC-XLSService!";
	}

	@RequestMapping(value = "/CountryQuarterSummaryView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CountryQuarterSummaryView> get_CountryQuarterSummaryView() throws Exception {
		System.out.println("RESTViewServiceXLS::get_CountryQuarterSummaryView()");
		List<CountryQuarterSummaryView> viewList = this.countryQuarterSummaryViewBuilder.build().getViewList();
		return viewList;
	}

	@Autowired
	private CountryQuarterSummaryViewBuilder countryQuarterSummaryViewBuilder;
}