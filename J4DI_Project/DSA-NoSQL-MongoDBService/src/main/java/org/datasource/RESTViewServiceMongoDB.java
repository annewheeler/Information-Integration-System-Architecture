package org.datasource;

import org.datasource.mongodb.views.countryprofiles.CountryAiProfileView;
import org.datasource.mongodb.views.countryprofiles.CountryAiProfileViewBuilder;
import org.datasource.mongodb.views.countryprofiles.CountryIndustryBenchmarkView;
import org.datasource.mongodb.views.countryprofiles.CountryIndustryBenchmarkViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
/*
    REST Service URL - MongoDB AI Country Profiles source
    Ping:
    http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/ping

    MongoDB source views:
    http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryAiProfileView
    http://localhost:8093/DSA-NoSQL-MongoDBService/rest/profiles/CountryIndustryBenchmarkView
*/
@RestController
@RequestMapping("/profiles")
public class RESTViewServiceMongoDB {

	private static Logger logger = Logger.getLogger(RESTViewServiceMongoDB.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> REST MongoDB Data Source is Up!");
		return "Ping response from RESTViewServiceMongoDB!";
	}

	@RequestMapping(value = "/CountryAiProfileView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CountryAiProfileView> get_CountryAiProfileView() throws Exception {
		System.out.println("RESTViewServiceMongoDB::get_CountryAiProfileView()");
		List<CountryAiProfileView> viewList = this.countryAiProfileViewBuilder.build().getCountryAiProfileViewList();
		return viewList;
	}
	@RequestMapping(value = "/CountryIndustryBenchmarkView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CountryIndustryBenchmarkView> get_CountryIndustryBenchmarkView() throws Exception {
		System.out.println("RESTViewServiceMongoDB::get_CountryIndustryBenchmarkView()");
		List<CountryIndustryBenchmarkView> viewList =
				this.countryIndustryBenchmarkViewBuilder.build().getCountryIndustryBenchmarkViewList();
		return viewList;
	}
	@Autowired
	private CountryAiProfileViewBuilder countryAiProfileViewBuilder;
	@Autowired
	private CountryIndustryBenchmarkViewBuilder countryIndustryBenchmarkViewBuilder;
}