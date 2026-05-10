package org.datasource;

import org.datasource.jdbc.views.adoption.CompanyAiAdoptionPgView;
import org.datasource.jdbc.views.adoption.CompanyAiAdoptionPgViewBuilder;
import org.datasource.jdbc.views.companies.CompaniesPgView;
import org.datasource.jdbc.views.companies.CompaniesPgViewBuilder;
import org.datasource.jdbc.views.governance.CompanyAiGovernancePgView;
import org.datasource.jdbc.views.governance.CompanyAiGovernancePgViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*
    DSA-SQL-JDBCService - PostgreSQL Access Model

    Ping:
    http://localhost:8090/DSA-SQL-JDBCService/rest/ping

    PostgreSQL endpoints:

    COMPANIES:
    http://localhost:8090/DSA-SQL-JDBCService/rest/companies/CompaniesPgView
    http://localhost:8090/DSA-SQL-JDBCService/rest/companies/CompaniesPgViewData?fetch_offset=1&fetch_size=10

    AI ADOPTION:
    http://localhost:8090/DSA-SQL-JDBCService/rest/adoption/CompanyAiAdoptionPgView
    http://localhost:8090/DSA-SQL-JDBCService/rest/adoption/CompanyAiAdoptionPgViewData?fetch_offset=1&fetch_size=10

    AI GOVERNANCE:
    http://localhost:8090/DSA-SQL-JDBCService/rest/governance/CompanyAiGovernancePgView
    http://localhost:8090/DSA-SQL-JDBCService/rest/governance/CompanyAiGovernancePgViewData?fetch_offset=1&fetch_size=10
*/
@RestController
public class RESTViewServiceJDBC {
	private static Logger logger = Logger.getLogger(RESTViewServiceJDBC.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String ping() {
		logger.info(">>>> DSA-SQL-JDBCService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JDBCService!";
	}

	// -------------------------------------------------------------------------
	// COMPANIES_PG
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/companies/CompaniesPgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompaniesPgView> get_CompaniesPgView() {
		List<CompaniesPgView> viewList = companiesPgViewBuilder
				.build()
				.getViewList();

		return viewList;
	}

	@RequestMapping(value = "/companies/CompaniesPgViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompaniesPgView> get_CompaniesPgView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompaniesPgView> viewList = companiesPgViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// COMPANY_AI_ADOPTION_PG
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/adoption/CompanyAiAdoptionPgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiAdoptionPgView> get_CompanyAiAdoptionPgView() {
		List<CompanyAiAdoptionPgView> viewList = companyAiAdoptionPgViewBuilder
				.build()
				.getViewList();

		return viewList;
	}

	@RequestMapping(value = "/adoption/CompanyAiAdoptionPgViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiAdoptionPgView> get_CompanyAiAdoptionPgView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompanyAiAdoptionPgView> viewList = companyAiAdoptionPgViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// COMPANY_AI_GOVERNANCE_PG
	// -------------------------------------------------------------------------

	@RequestMapping(value = "/governance/CompanyAiGovernancePgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiGovernancePgView> get_CompanyAiGovernancePgView() {
		List<CompanyAiGovernancePgView> viewList = companyAiGovernancePgViewBuilder
				.build()
				.getViewList();

		return viewList;
	}

	@RequestMapping(value = "/governance/CompanyAiGovernancePgViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiGovernancePgView> get_CompanyAiGovernancePgView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
	) {
		List<CompanyAiGovernancePgView> viewList = companyAiGovernancePgViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build()
				.getViewList();

		return viewList;
	}

	// -------------------------------------------------------------------------
	// Builders
	// -------------------------------------------------------------------------

	@Autowired private CompaniesPgViewBuilder companiesPgViewBuilder;
	@Autowired private CompanyAiAdoptionPgViewBuilder companyAiAdoptionPgViewBuilder;
	@Autowired private CompanyAiGovernancePgViewBuilder companyAiGovernancePgViewBuilder;
}