package org.j4di;

import org.j4di.access.views.postgres.COMPANIES_PG_V;
import org.j4di.access.views.postgres.COMPANIES_PG_V_Repository;
import org.j4di.access.views.postgres.COMPANY_AI_ADOPTION_PG_V;
import org.j4di.access.views.postgres.COMPANY_AI_ADOPTION_PG_V_Repository;
import org.j4di.access.views.postgres.COMPANY_AI_GOVERNANCE_PG_V;
import org.j4di.access.views.postgres.COMPANY_AI_GOVERNANCE_PG_V_Repository;

import org.j4di.access.views.oracle.COMPANIES_VIEW;
import org.j4di.access.views.oracle.COMPANIES_VIEW_Repository;
import org.j4di.access.views.oracle.COMPANY_BUSINESS_OUTCOMES_VIEW;
import org.j4di.access.views.oracle.COMPANY_BUSINESS_OUTCOMES_VIEW_Repository;
import org.j4di.access.views.oracle.COMPANY_WORKFORCE_IMPACT_VIEW;
import org.j4di.access.views.oracle.COMPANY_WORKFORCE_IMPACT_VIEW_Repository;
import org.j4di.access.views.excel.XLS_COUNTRY_QUARTER_SUMMARY_V;
import org.j4di.access.views.excel.XLS_COUNTRY_QUARTER_SUMMARY_V_Repository;
import org.j4di.access.views.mongo.MONGO_COUNTRY_AI_PROFILE_V;
import org.j4di.access.views.mongo.MONGO_COUNTRY_AI_PROFILE_V_Repository;
import org.j4di.access.views.mongo.MONGO_COUNTRY_INDUSTRY_BENCHMARK_V;
import org.j4di.access.views.mongo.MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Repository;

import org.j4di.integration.views.CONS_PG_ORCL_EVENT_V;
import org.j4di.integration.views.CONS_PG_ORCL_EVENT_V_Repository;
import org.j4di.integration.views.CONS_PG_MONGO_COUNTRY_INDUSTRY_V;
import org.j4di.integration.views.CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Repository;
import org.j4di.integration.views.CONS_PG_XLS_COUNTRY_TIME_V;
import org.j4di.integration.views.CONS_PG_XLS_COUNTRY_TIME_V_Repository;
import org.j4di.integration.views.CONS_ORCL_PG_XLS_COUNTRY_TIME_V;
import org.j4di.integration.views.CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Repository;
import org.j4di.integration.views.CONS_GLOBAL_COUNTRY_TIME_V;
import org.j4di.integration.views.CONS_GLOBAL_COUNTRY_TIME_V_Repository;
import org.j4di.ROLAP.fact.dimensions.FACT_COMPANY_AI_EVENT_V;
import org.j4di.ROLAP.fact.dimensions.FACT_COMPANY_AI_EVENT_V_Repository;
import org.j4di.ROLAP.fact.dimensions.DIM_TIME_V;
import org.j4di.ROLAP.fact.dimensions.DIM_TIME_V_Repository;
import org.j4di.ROLAP.fact.dimensions.DIM_COMPANY_V;
import org.j4di.ROLAP.fact.dimensions.DIM_COMPANY_V_Repository;
import org.j4di.ROLAP.fact.dimensions.DIM_COUNTRY_V;
import org.j4di.ROLAP.fact.dimensions.DIM_COUNTRY_V_Repository;
import org.j4di.ROLAP.fact.dimensions.DIM_INDUSTRY_V;
import org.j4di.ROLAP.fact.dimensions.DIM_INDUSTRY_V_Repository;
import org.j4di.OLAP.Analytical.OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V;
import org.j4di.OLAP.Analytical.OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V_Repository;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Repository;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Repository;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V;
import org.j4di.OLAP.Analytical.OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V_Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*
    REST Service URL - AI Data Service Architecture WEB Model

    Ping:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/ping

    Access Views - PostgreSQL:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANIES_PG_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANY_AI_ADOPTION_PG_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANY_AI_GOVERNANCE_PG_V

    Access Views - Oracle:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANIES_VIEW
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANY_BUSINESS_OUTCOMES_VIEW
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/COMPANY_WORKFORCE_IMPACT_VIEW

    Access Views - Excel:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/XLS_COUNTRY_QUARTER_SUMMARY_V

    Access Views - MongoDB:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/MONGO_COUNTRY_AI_PROFILE_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/MONGO_COUNTRY_INDUSTRY_BENCHMARK_V

    Integration / Consolidation Views:
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/CONS_PG_ORCL_EVENT_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/CONS_PG_MONGO_COUNTRY_INDUSTRY_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/CONS_PG_XLS_COUNTRY_TIME_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/CONS_ORCL_PG_XLS_COUNTRY_TIME_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/CONS_GLOBAL_COUNTRY_TIME_V

    Fact + Dimensions Views
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/DIM_TIME_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/DIM_COMPANY_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/DIM_COUNTRY_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/DIM_INDUSTRY_V

    OLAP + Analytical Views
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V
    http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V

*/
@RestController
@RequestMapping("/OLAP")
public class RESTViewService {

	private static final Logger logger = Logger.getLogger(RESTViewService.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-WEB-RESTService:: RESTViewService is Up!");
		return "Ping response from DSA-WEB-RESTService!";
	}

	// -------------------------------------------------------------------------
	// Access Views - PostgreSQL
	// -------------------------------------------------------------------------

	@Autowired
	private COMPANIES_PG_V_Repository COMPANIES_PG_V_Repository;

	@Autowired
	private COMPANY_AI_ADOPTION_PG_V_Repository COMPANY_AI_ADOPTION_PG_V_Repository;

	@Autowired
	private COMPANY_AI_GOVERNANCE_PG_V_Repository COMPANY_AI_GOVERNANCE_PG_V_Repository;

	@GetMapping(value = "/COMPANIES_PG_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANIES_PG_V> get_COMPANIES_PG_V() {
		return this.COMPANIES_PG_V_Repository.get_COMPANIES_PG_V();
	}

	@GetMapping(value = "/COMPANY_AI_ADOPTION_PG_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANY_AI_ADOPTION_PG_V> get_COMPANY_AI_ADOPTION_PG_V() {
		return this.COMPANY_AI_ADOPTION_PG_V_Repository.get_COMPANY_AI_ADOPTION_PG_V();
	}

	@GetMapping(value = "/COMPANY_AI_GOVERNANCE_PG_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANY_AI_GOVERNANCE_PG_V> get_COMPANY_AI_GOVERNANCE_PG_V() {
		return this.COMPANY_AI_GOVERNANCE_PG_V_Repository.get_COMPANY_AI_GOVERNANCE_PG_V();
	}

	// -------------------------------------------------------------------------
	// Access Views - Oracle
	// -------------------------------------------------------------------------

	@Autowired
	private COMPANIES_VIEW_Repository COMPANIES_VIEW_Repository;

	@Autowired
	private COMPANY_BUSINESS_OUTCOMES_VIEW_Repository COMPANY_BUSINESS_OUTCOMES_VIEW_Repository;

	@Autowired
	private COMPANY_WORKFORCE_IMPACT_VIEW_Repository COMPANY_WORKFORCE_IMPACT_VIEW_Repository;

	@GetMapping(value = "/COMPANIES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANIES_VIEW> get_COMPANIES_VIEW() {
		return this.COMPANIES_VIEW_Repository.get_COMPANIES_VIEW();
	}

	@GetMapping(value = "/COMPANY_BUSINESS_OUTCOMES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANY_BUSINESS_OUTCOMES_VIEW> get_COMPANY_BUSINESS_OUTCOMES_VIEW() {
		return this.COMPANY_BUSINESS_OUTCOMES_VIEW_Repository.get_COMPANY_BUSINESS_OUTCOMES_VIEW();
	}

	@GetMapping(value = "/COMPANY_WORKFORCE_IMPACT_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<COMPANY_WORKFORCE_IMPACT_VIEW> get_COMPANY_WORKFORCE_IMPACT_VIEW() {
		return this.COMPANY_WORKFORCE_IMPACT_VIEW_Repository.get_COMPANY_WORKFORCE_IMPACT_VIEW();
	}
	// -------------------------------------------------------------------------
	// Access Views - Excel
	// -------------------------------------------------------------------------
	@Autowired
	private XLS_COUNTRY_QUARTER_SUMMARY_V_Repository XLS_COUNTRY_QUARTER_SUMMARY_V_Repository;

	@GetMapping(value = "/XLS_COUNTRY_QUARTER_SUMMARY_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<XLS_COUNTRY_QUARTER_SUMMARY_V> get_XLS_COUNTRY_QUARTER_SUMMARY_V() {
		return this.XLS_COUNTRY_QUARTER_SUMMARY_V_Repository.get_XLS_COUNTRY_QUARTER_SUMMARY_V();
	}

	// -------------------------------------------------------------------------
    // Access Views - MongoDB
    // -------------------------------------------------------------------------

	@Autowired
	private MONGO_COUNTRY_AI_PROFILE_V_Repository MONGO_COUNTRY_AI_PROFILE_V_Repository;

	@Autowired
	private MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Repository MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Repository;

	@GetMapping(value = "/MONGO_COUNTRY_AI_PROFILE_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MONGO_COUNTRY_AI_PROFILE_V> get_MONGO_COUNTRY_AI_PROFILE_V() {
		return this.MONGO_COUNTRY_AI_PROFILE_V_Repository.get_MONGO_COUNTRY_AI_PROFILE_V();
	}

	@GetMapping(value = "/MONGO_COUNTRY_INDUSTRY_BENCHMARK_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<MONGO_COUNTRY_INDUSTRY_BENCHMARK_V> get_MONGO_COUNTRY_INDUSTRY_BENCHMARK_V() {
		return this.MONGO_COUNTRY_INDUSTRY_BENCHMARK_V_Repository.get_MONGO_COUNTRY_INDUSTRY_BENCHMARK_V();
	}

	// -------------------------------------------------------------------------
    // Integration / Consolidation Views
    // -------------------------------------------------------------------------

	@Autowired
	private CONS_PG_ORCL_EVENT_V_Repository CONS_PG_ORCL_EVENT_V_Repository;

	@GetMapping(value = "/CONS_PG_ORCL_EVENT_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CONS_PG_ORCL_EVENT_V> get_CONS_PG_ORCL_EVENT_V() {
		return this.CONS_PG_ORCL_EVENT_V_Repository.get_CONS_PG_ORCL_EVENT_V();
	}

	@Autowired
	private CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Repository CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Repository;

	@GetMapping(value = "/CONS_PG_MONGO_COUNTRY_INDUSTRY_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CONS_PG_MONGO_COUNTRY_INDUSTRY_V> get_CONS_PG_MONGO_COUNTRY_INDUSTRY_V() {
		return this.CONS_PG_MONGO_COUNTRY_INDUSTRY_V_Repository.get_CONS_PG_MONGO_COUNTRY_INDUSTRY_V();
	}

	@Autowired
	private CONS_PG_XLS_COUNTRY_TIME_V_Repository CONS_PG_XLS_COUNTRY_TIME_V_Repository;

	@GetMapping(value = "/CONS_PG_XLS_COUNTRY_TIME_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CONS_PG_XLS_COUNTRY_TIME_V> get_CONS_PG_XLS_COUNTRY_TIME_V() {
		return this.CONS_PG_XLS_COUNTRY_TIME_V_Repository.get_CONS_PG_XLS_COUNTRY_TIME_V();
	}

	@Autowired
	private CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Repository CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Repository;

	@GetMapping(value = "/CONS_ORCL_PG_XLS_COUNTRY_TIME_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CONS_ORCL_PG_XLS_COUNTRY_TIME_V> get_CONS_ORCL_PG_XLS_COUNTRY_TIME_V() {
		return this.CONS_ORCL_PG_XLS_COUNTRY_TIME_V_Repository.get_CONS_ORCL_PG_XLS_COUNTRY_TIME_V();
	}

	@Autowired
	private CONS_GLOBAL_COUNTRY_TIME_V_Repository CONS_GLOBAL_COUNTRY_TIME_V_Repository;

	@GetMapping(value = "/CONS_GLOBAL_COUNTRY_TIME_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<CONS_GLOBAL_COUNTRY_TIME_V> get_CONS_GLOBAL_COUNTRY_TIME_V() {
		return this.CONS_GLOBAL_COUNTRY_TIME_V_Repository.get_CONS_GLOBAL_COUNTRY_TIME_V();
	}

	@Autowired
	private FACT_COMPANY_AI_EVENT_V_Repository FACT_COMPANY_AI_EVENT_V_Repository;

	@GetMapping(value = "/FACT_COMPANY_AI_EVENT_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<FACT_COMPANY_AI_EVENT_V> get_FACT_COMPANY_AI_EVENT_V() {
		return this.FACT_COMPANY_AI_EVENT_V_Repository.get_FACT_COMPANY_AI_EVENT_V();
	}

	@Autowired
	private DIM_TIME_V_Repository DIM_TIME_V_Repository;

	@GetMapping(value = "/DIM_TIME_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DIM_TIME_V> get_DIM_TIME_V() {
		return this.DIM_TIME_V_Repository.get_DIM_TIME_V();
	}
	@Autowired
	private DIM_COMPANY_V_Repository DIM_COMPANY_V_Repository;

	@GetMapping(value = "/DIM_COMPANY_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DIM_COMPANY_V> get_DIM_COMPANY_V() {
		return this.DIM_COMPANY_V_Repository.get_DIM_COMPANY_V();
	}

	@Autowired
	private DIM_COUNTRY_V_Repository DIM_COUNTRY_V_Repository;

	@GetMapping(value = "/DIM_COUNTRY_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DIM_COUNTRY_V> get_DIM_COUNTRY_V() {
		return this.DIM_COUNTRY_V_Repository.get_DIM_COUNTRY_V();
	}

	@Autowired
	private DIM_INDUSTRY_V_Repository DIM_INDUSTRY_V_Repository;

	@GetMapping(value = "/DIM_INDUSTRY_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<DIM_INDUSTRY_V> get_DIM_INDUSTRY_V() {
		return this.DIM_INDUSTRY_V_Repository.get_DIM_INDUSTRY_V();
	}
	@Autowired
	private OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V_Repository OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V_Repository;

	@GetMapping(value = "/OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V> get_OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V() {
		return this.OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V_Repository.get_OLAP_VIEW_AI_COUNTRY_TIME_WINDOW_V();
	}
	@Autowired
	private OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Repository OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Repository;

	@GetMapping(value = "/OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V> get_OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V() {
		return this.OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V_Repository.get_OLAP_VIEW_COUNTRY_INDUSTRY_CUBE_V();
	}

	@Autowired
	private OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Repository OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Repository;

	@GetMapping(value = "/OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V> get_OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V() {
		return this.OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V_Repository.get_OLAP_VIEW_COMPANY_SIZE_COUNTRY_GROUPSETS_V();
	}

	@Autowired
	private OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V_Repository OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V_Repository;

	@GetMapping(value = "/OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V> get_OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V() {
		return this.OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V_Repository.get_OLAP_VIEW_COUNTRY_BENCHMARK_VARIANCE_V();
	}
}