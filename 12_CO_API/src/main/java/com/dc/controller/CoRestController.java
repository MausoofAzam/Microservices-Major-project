package com.dc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dc.binding.CoResponse;
import com.dc.service.CoService;

@RestController
public class CoRestController {

	@Autowired
	private CoService coService;

	@GetMapping("/process")
	public CoResponse processTriggers() {

		return coService.processPendingTriggers();
	}
}
