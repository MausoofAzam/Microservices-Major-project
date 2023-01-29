package com.insorance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insorance.binding.Education;
import com.insorance.service.DcService;

@RestController
public class EducationRestController {

	@Autowired
	private DcService service;

	@PostMapping("/education")
	public ResponseEntity<Long> saveEducation(@RequestBody Education education) {
		Long caseNum = service.saveEducation(education);

		return new ResponseEntity<>(caseNum, HttpStatus.CREATED);
	}
}
