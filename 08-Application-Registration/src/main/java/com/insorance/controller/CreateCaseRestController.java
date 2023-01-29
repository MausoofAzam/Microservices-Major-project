package com.insorance.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.insorance.binding.CreateCaseResponse;
import com.insorance.service.DcService;

@RestController
public class CreateCaseRestController {

	@Autowired
	private DcService dcService;

	/**Here in this method appId i am taking as a path varible,  based on app id 
	 * i am getting case number and i am getting the plan names, for case number and plans i have created one binding class
	 * here i am setting the data and i am sending the response.
	 * @param appId
	 * @return
	 */
	@GetMapping("/case/{appId}")
	public ResponseEntity<CreateCaseResponse> createCase(@PathVariable Integer appId) {
		Long caseNum = dcService.loadCaseNum(appId);
		Map<Integer, String> plansMap = dcService.getPlanNames();
		CreateCaseResponse response = new CreateCaseResponse();

		response.setCaseNum(caseNum);
		response.setPlanNames(plansMap);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
