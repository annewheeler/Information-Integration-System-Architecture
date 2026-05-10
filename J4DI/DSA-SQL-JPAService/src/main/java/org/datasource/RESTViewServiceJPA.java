package org.datasource;

import org.datasource.jpa.views.businessoutcomes.CompanyBusinessOutcomesOrclView;
import org.datasource.jpa.views.businessoutcomes.CompanyBusinessOutcomesOrclViewBuilder;
import org.datasource.jpa.views.companies.CompanyOrclView;
import org.datasource.jpa.views.companies.CompanyOrclViewBuilder;
import org.datasource.jpa.views.workforce.CompanyWorkforceImpactOrclView;
import org.datasource.jpa.views.workforce.CompanyWorkforceImpactOrclViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*
    DSA-SQL-JPAService - Oracle AI_DATA Access Model

    Ping:
    http://localhost:8091/DSA_SQL_JPAService/rest/ping

    Oracle endpoints:

    COMPANIES:
    http://localhost:8091/DSA_SQL_JPAService/rest/companies/CompanyOrclView
    http://localhost:8091/DSA_SQL_JPAService/rest/companies/CompanyOrclViewData?fetch_offset=1&fetch_size=10

    BUSINESS OUTCOMES:
    http://localhost:8091/DSA_SQL_JPAService/rest/businessoutcomes/CompanyBusinessOutcomesOrclView
    http://localhost:8091/DSA_SQL_JPAService/rest/businessoutcomes/CompanyBusinessOutcomesOrclViewData?fetch_offset=1&fetch_size=10

    WORKFORCE IMPACT:
    http://localhost:8091/DSA_SQL_JPAService/rest/workforce/CompanyWorkforceImpactOrclView
    http://localhost:8091/DSA_SQL_JPAService/rest/workforce/CompanyWorkforceImpactOrclViewData?fetch_offset=1&fetch_size=10
*/
@RestController
public class RESTViewServiceJPA {
	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService!";
	}

	// -------------------------------------------------------------------------
	// AI_DATA.COMPANIES
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/companies/CompanyOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyOrclView> get_CompanyOrclView() {
		List<CompanyOrclView> viewList = companyOrclViewBuilder
				.build()
				.getCompanyOrclViewList();

		return viewList;
	}

	@RequestMapping(value = "/companies/CompanyOrclViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyOrclView> get_CompanyOrclView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompanyOrclView> viewList = companyOrclViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getCompanyOrclViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// AI_DATA.COMPANY_BUSINESS_OUTCOMES
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/businessoutcomes/CompanyBusinessOutcomesOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyBusinessOutcomesOrclView> get_CompanyBusinessOutcomesOrclView() {
		List<CompanyBusinessOutcomesOrclView> viewList = companyBusinessOutcomesOrclViewBuilder
				.build()
				.getCompanyBusinessOutcomesOrclViewList();

		return viewList;
	}

	@RequestMapping(value = "/businessoutcomes/CompanyBusinessOutcomesOrclViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyBusinessOutcomesOrclView> get_CompanyBusinessOutcomesOrclView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompanyBusinessOutcomesOrclView> viewList = companyBusinessOutcomesOrclViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getCompanyBusinessOutcomesOrclViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// AI_DATA.COMPANY_WORKFORCE_IMPACT
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/workforce/CompanyWorkforceImpactOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyWorkforceImpactOrclView> get_CompanyWorkforceImpactOrclView() {
		List<CompanyWorkforceImpactOrclView> viewList = companyWorkforceImpactOrclViewBuilder
				.build()
				.getCompanyWorkforceImpactOrclViewList();

		return viewList;
	}

	@RequestMapping(value = "/workforce/CompanyWorkforceImpactOrclViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyWorkforceImpactOrclView> get_CompanyWorkforceImpactOrclView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompanyWorkforceImpactOrclView> viewList = companyWorkforceImpactOrclViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getCompanyWorkforceImpactOrclViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// Builders
	// -------------------------------------------------------------------------

	@Autowired
	private CompanyOrclViewBuilder companyOrclViewBuilder;

	@Autowired
	private CompanyBusinessOutcomesOrclViewBuilder companyBusinessOutcomesOrclViewBuilder;

	@Autowired
	private CompanyWorkforceImpactOrclViewBuilder companyWorkforceImpactOrclViewBuilder;
}