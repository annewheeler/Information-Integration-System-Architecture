package org.datasource;

import org.datasource.jdbc.JDBCDataSourceConnector;
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
    REST Service URL - PostgreSQL AI Adoption source

    Ping:
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/ping

    PostgreSQL source views:
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompaniesPgView
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiAdoptionPgView
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiGovernancePgView

    Optional limit parameter:
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompaniesPgView?limit=1000
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiAdoptionPgView?limit=3000
    http://localhost:8090/DSA-SQL-JDBCService/rest/postgresql/CompanyAiGovernancePgView?limit=7000
*/
@RestController
@RequestMapping("/postgresql")
public class RESTViewServiceJDBC {
	private static Logger logger = Logger.getLogger(RESTViewServiceJDBC.class.getName());

	/*
        Endpoint simplu de test.
        Acesta verifică doar că serviciul Spring Boot este pornit.
        Nu interoghează baza PostgreSQL.
    */
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String ping() {
		logger.info(">>>> DSA-SQL-JDBCService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JDBCService!";
	}

	/*
        Endpoint pentru tabela PostgreSQL:
        public.companies_pg

        Dacă limit este null, builderul folosește DEFAULT_LIMIT = 1000.
        Dacă limit depășește MAX_LIMIT = 7000, builderul îl reduce automat la 7000.
    */
	@RequestMapping(value = "/CompaniesPgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompaniesPgView> get_CompaniesPgView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompaniesPgView> viewList = companiesPgViewBuilder
				.build(limit)
				.getViewList();

		return viewList;
	}

	/*
        Endpoint pentru tabela PostgreSQL:
        public.company_ai_adoption_pg
    */
	@RequestMapping(value = "/CompanyAiAdoptionPgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiAdoptionPgView> get_CompanyAiAdoptionPgView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompanyAiAdoptionPgView> viewList = companyAiAdoptionPgViewBuilder
				.build(limit)
				.getViewList();

		return viewList;
	}

	/*
        Endpoint pentru tabela PostgreSQL:
        public.company_ai_governance_pg
    */
	@RequestMapping(value = "/CompanyAiGovernancePgView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyAiGovernancePgView> get_CompanyAiGovernancePgView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompanyAiGovernancePgView> viewList = companyAiGovernancePgViewBuilder
				.build(limit)
				.getViewList();

		return viewList;
	}

	// Set-up ------------------------------------------------------------

	/*
        Păstrăm connectorul injectat pentru consistență cu modelul profesorului.
        Builder-ele folosesc efectiv JDBCDataSourceConnector pentru conexiunea PostgreSQL.
    */
	@Autowired private JDBCDataSourceConnector jdbcConnector;

	@Autowired private CompaniesPgViewBuilder companiesPgViewBuilder;
	@Autowired private CompanyAiAdoptionPgViewBuilder companyAiAdoptionPgViewBuilder;
	@Autowired private CompanyAiGovernancePgViewBuilder companyAiGovernancePgViewBuilder;
}