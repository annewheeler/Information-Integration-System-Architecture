package org.datasource;

import org.datasource.jpa.JPADataSourceConnector;
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
    REST Service URL - Oracle AI_DATA source

    Ping:
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/ping

    Oracle source views:
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyOrclView
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyBusinessOutcomesOrclView
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyWorkforceImpactOrclView

    Optional limit parameter:
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyOrclView?limit=1000
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyBusinessOutcomesOrclView?limit=3000
    http://localhost:8091/DSA_SQL_JPAService/rest/oracle/CompanyWorkforceImpactOrclView?limit=7000
*/
@RestController
@RequestMapping("/oracle")
public class RESTViewServiceJPA {
	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());

	/*
        Endpoint simplu de test.
        Verifică doar că serviciul Spring Boot JPA este pornit.
        Nu interoghează Oracle.
    */
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService!";
	}

	/*
        Endpoint pentru tabela Oracle:
        AI_DATA.COMPANIES

        Dacă limit este null, builderul folosește DEFAULT_LIMIT = 1000.
        Dacă limit depășește MAX_LIMIT = 7000, builderul îl reduce automat la 7000.
    */
	@RequestMapping(value = "/CompanyOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyOrclView> get_CompanyOrclView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompanyOrclView> viewList = companyOrclViewBuilder
				.build(limit)
				.getCompanyOrclViewList();

		return viewList;
	}

	/*
        Endpoint pentru tabela Oracle:
        AI_DATA.COMPANY_BUSINESS_OUTCOMES
    */
	@RequestMapping(value = "/CompanyBusinessOutcomesOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyBusinessOutcomesOrclView> get_CompanyBusinessOutcomesOrclView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompanyBusinessOutcomesOrclView> viewList =
				companyBusinessOutcomesOrclViewBuilder
						.build(limit)
						.getCompanyBusinessOutcomesOrclViewList();

		return viewList;
	}

	/*
        Endpoint pentru tabela Oracle:
        AI_DATA.COMPANY_WORKFORCE_IMPACT
    */
	@RequestMapping(value = "/CompanyWorkforceImpactOrclView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CompanyWorkforceImpactOrclView> get_CompanyWorkforceImpactOrclView(
			@RequestParam(value = "limit", required = false) Integer limit
	) {
		List<CompanyWorkforceImpactOrclView> viewList =
				companyWorkforceImpactOrclViewBuilder
						.build(limit)
						.getCompanyWorkforceImpactOrclViewList();

		return viewList;
	}

	// Set-up ------------------------------------------------------------

	/*
        Păstrăm connectorul injectat pentru consistență cu modelul profesorului.
        Builder-ele folosesc efectiv EntityManager-ul din JPADataSourceConnector.
    */
	@Autowired private JPADataSourceConnector dataSourceConnector;

	@Autowired private CompanyOrclViewBuilder companyOrclViewBuilder;
	@Autowired private CompanyBusinessOutcomesOrclViewBuilder companyBusinessOutcomesOrclViewBuilder;
	@Autowired private CompanyWorkforceImpactOrclViewBuilder companyWorkforceImpactOrclViewBuilder;
}