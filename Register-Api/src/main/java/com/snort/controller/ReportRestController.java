package com.snort.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snort.request.SearchRequest;
import com.snort.request.SearchResponse;
import com.snort.service.EligibilityService;

@RestController
public class ReportRestController {

	@Autowired
	private EligibilityService service;

	/**
	 * this is get request method, what this method will do,this method will talk to
	 * service layer get the unique plan name in the table; those unique plan name
	 * we are returning in the response.
	 * 
	 * @return
	 */
	@GetMapping("/plans")
	public ResponseEntity<List<String>> getPlanName() {
		List<String> planName = service.getUniquePlanName();
		return new ResponseEntity<List<String>>(planName, HttpStatus.OK);
	}

	/**
	 * this method will get the data from from service layer i.e get plan statuses
	 * and give return response of plan status in the table whatever the response we
	 * are getting those unique plan statuses we will set as response to the ui,
	 * that data can be displayed in the dropdown.
	 * 
	 * @return
	 */
	@GetMapping("/statuses")
	public ResponseEntity<List<String>> getPlanStasuses() {
		List<String> planStatus = service.getUniquePlanStatus();

		return new ResponseEntity<List<String>>(planStatus, HttpStatus.OK);
	}

	/**
	 * this method is post method that simply call service layer method as
	 * SearchRequest whatever the user is selecting fields that data will come as
	 * SearchRequest, that search request data we are selecting as request body
	 * whatever the body request data we are giving as parameter to service layer
	 * method. service layer returning list of search response object the list of
	 * search object we are sending as the response body.
	 * 
	 * @param searchRequest
	 * @return
	 */
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest searchRequest) {
		List<SearchResponse> response = service.search(searchRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Here when the client is directly sending the request, when the user click on
	 * download excel button then server will sent response and generate a excel
	 * file and sent the response to the user this method is used to download data
	 * in the form of excel the response method method we are passing to service
	 * method, service layer method will write the data, it will generate excel as
	 * workbook and it will store into output stream, response will come directly to
	 * the attachment and file will be downloaded directly.
	 * 
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.xlsx";

		response.setHeader(headerKey, headerValue);
		service.generateExcel(response);
	}

	/**
	 * This method is a get mapping method which is used to get the pdf attachment,
	 * when the user send request while clicking on download pdf file then a request
	 * will generated in the form of HTTP Servlet Response and this service method
	 * directly generate and download the pdf file from the server.
	 * 
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/pdf")
	public void pdfreport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.pdf";
		// here header will tell in which format the request is coming to the client.
		response.setHeader(headerKey, headerValue);
		service.generatePdf(response);

	}

}
