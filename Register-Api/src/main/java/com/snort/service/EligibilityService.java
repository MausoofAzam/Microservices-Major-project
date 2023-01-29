package com.snort.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.snort.request.SearchRequest;
import com.snort.request.SearchResponse;

public interface EligibilityService {

	public List<String> getUniquePlanName();
	
	public List<String> getUniquePlanStatus();
	
	public List<SearchResponse> search(SearchRequest request);
	
	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws  Exception;
}