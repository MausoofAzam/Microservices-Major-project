package com.insorance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insorance.binding.ChildRequest;
import com.insorance.binding.DcSummary;
import com.insorance.service.DcService;

@RestController
public class ChildRestController {

	@Autowired
	private DcService service;

	@PostMapping("/children")
	public ResponseEntity<DcSummary> saveChilds(@RequestBody ChildRequest request) {

		Long caseNum = service.saveChildren(request);
		
		DcSummary summary = service.getSummary(caseNum);

		return new ResponseEntity<>(summary, HttpStatus.OK);
	}

}
