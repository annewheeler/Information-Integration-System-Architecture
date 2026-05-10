package org.j4di;

import org.j4di.access.views.invoices.INVOICES_SALES_VIEW;
import org.j4di.access.views.invoices.INVOICES_VIEW;
import org.j4di.access.views.invoices.INVOICES_VIEW_Repository;
import org.j4di.analytical.views.OLAP_VIEW_SALES_DEP_CIT_CUST;
import org.j4di.analytical.views.OLAP_VIEW_SALES_DEP_CIT_CUST_Repository;
import org.j4di.integration.views.OLAP_DIM_CUSTS_CITIES_DEPTS;
import org.j4di.integration.views.OLAP_DIM_CUSTS_CITIES_DEPTS_Repository;
import org.j4di.integration.views.OLAP_FACTS_SALES_AMOUNT;
import org.j4di.integration.views.OLAP_FACTS_SALES_AMOUNT_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/INVOICES_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/INVOICES_VIEW/1001
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/INVOICES_SALES_VIEW

	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_DIM_CUSTS_CITIES_DEPTS
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_FACTS_SALES_AMOUNT
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/OLAP_VIEW_SALES_DEP_CIT_CUST
*/
@RestController
@RequestMapping("/OLAP")
public class RESTViewService {
	private static Logger logger = Logger.getLogger(RESTViewService.class.getName());
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-WEB-SparkService:: RESTViewService is Up!");
		return "Ping response from DSA-WEB-SparkService!";
	}
	
	// Set-up view builder
	@Autowired private INVOICES_VIEW_Repository INVOICES_VIEW_Repository;
	// View endpoints
	@GetMapping(value = "/INVOICES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<INVOICES_VIEW> get_INVOICES_VIEW() {
		List<INVOICES_VIEW> viewList =
				//this.invoiceViewRepository.findAll(); // NOT Working
				this.INVOICES_VIEW_Repository.get_INVOICES_VIEW();
		return viewList;
	}

	@GetMapping(value = "/INVOICES_VIEW/{customerId}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<INVOICES_VIEW> get_INVOICES_VIEW_ByCustomerId(@PathVariable Long customerId) {
		List<INVOICES_VIEW> viewList = this.INVOICES_VIEW_Repository.get_INVOICES_VIEW_ByCustomerId(customerId);
		return viewList;
	}

	@GetMapping(value = "/INVOICES_SALES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<INVOICES_SALES_VIEW> get_InvoicesSalesView() {
		List<INVOICES_SALES_VIEW> viewList = this.INVOICES_VIEW_Repository.get_INVOICES_SALES_VIEW();
		return viewList;
	}

	// Integration views REST Enpoints
	@Autowired private OLAP_DIM_CUSTS_CITIES_DEPTS_Repository OLAP_DIM_CUSTS_CITIES_DEPTS_Repository;
	@Autowired private OLAP_FACTS_SALES_AMOUNT_Repository OLAP_FACTS_SALES_AMOUNT_Repository;

	@GetMapping(value = "/OLAP_DIM_CUSTS_CITIES_DEPTS",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_DIM_CUSTS_CITIES_DEPTS> get_OLAP_DIM_CUSTS_CITIES_DEPTS() {
		List<OLAP_DIM_CUSTS_CITIES_DEPTS> viewList = this.OLAP_DIM_CUSTS_CITIES_DEPTS_Repository.get_OLAP_DIM_CUSTS_CITIES_DEPTS();
		return viewList;
	}

	@GetMapping(value = "/OLAP_FACTS_SALES_AMOUNT",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_FACTS_SALES_AMOUNT> get_OLAP_FACTS_SALES_AMOUNT() {
		List<OLAP_FACTS_SALES_AMOUNT> viewList = this.OLAP_FACTS_SALES_AMOUNT_Repository.get_OLAP_FACTS_SALES_AMOUNT();
		return viewList;
	}

	// Analytical views REST Enpoints
	@Autowired private OLAP_VIEW_SALES_DEP_CIT_CUST_Repository OLAP_VIEW_SALES_DEP_CIT_CUST_Repository;

	@GetMapping(value = "/OLAP_VIEW_SALES_DEP_CIT_CUST",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<OLAP_VIEW_SALES_DEP_CIT_CUST> get_OLAP_VIEW_SALES_DEP_CIT_CUST() {
		List<OLAP_VIEW_SALES_DEP_CIT_CUST> viewList = this.OLAP_VIEW_SALES_DEP_CIT_CUST_Repository.get_OLAP_VIEW_SALES_DEP_CIT_CUST();
		return viewList;
	}
}