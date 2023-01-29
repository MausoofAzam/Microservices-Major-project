package com.insorance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insorance.binding.CitizenApp;
import com.insorance.service.AppRegService;

@RestController
public class AppRegRestController {

	@Autowired
	private AppRegService service;

	@PostMapping("/app")
	public ResponseEntity<String> createCitizenApp(@RequestBody CitizenApp app) {

		Integer appId = service.createApplication(app);

		if (appId > 0) {
			return new ResponseEntity<String>("App created with App_Id: " + appId, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Invalid SSN", HttpStatus.BAD_REQUEST);
		}

	}
}
