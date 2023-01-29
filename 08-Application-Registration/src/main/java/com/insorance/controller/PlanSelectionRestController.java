package com.insorance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insorance.binding.PlanSelection;
import com.insorance.service.DcService;

@RestController
public class PlanSelectionRestController {

	@Autowired
	private DcService dcService;

	/**
	 * this method is just for update the case number by plan id
	 * 
	 * @param planSel
	 * @return
	 */
	@PostMapping("/plansel")
	public ResponseEntity<Long> planSelection(@RequestBody PlanSelection planSel) {

		Long caseNum = dcService.savePlanSelection(planSel);

		return new ResponseEntity<>(caseNum, HttpStatus.CREATED);
	}
}
