package com.insorance.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.insorance.binding.CitizenApp;
import com.insorance.entity.CitizenAppEntity;
import com.insorance.repository.CitizenAppRepository;
import com.insorance.service.AppRegService;

@Service
public class AppRegServiceImpl implements AppRegService {

	@Autowired
	private CitizenAppRepository citizenAppRepo;

	/**
	 * in this method application will accept details of user then it will make a
	 * call to third party project based on the ssn number will the data that which
	 * citizen belongs to which state. if the user belongs to only new jersey then
	 * only application is created and it application id will be generated.based on
	 * ssn number we get state name otherwise his application should not be created.
	 *
	 */
	@Override
	public Integer createApplication(CitizenApp app) {

		// make rest call to ssa-web api with ssn input
		String endpointUrl = "https://ssa-web-api.herokuapp.com/ssn/{ssn}";

		/*
		 * RestTemplate rt = new RestTemplate();
		 * 
		 * ResponseEntity<String> resEntity = rt.getForEntity(endpointUrl, String.class,
		 * app.getSsn()); String stateName = resEntity.getBody();
		 */
		//Rest Template only supports synchronous calls, 
		//web Client supports synchronous and asynchronous calls
		WebClient webClient = WebClient.create();
		String stateName = webClient.get() //get request
				.uri(endpointUrl, app.getSsn()) //url to send request
				.retrieve() //to retrieve response
				.bodyToMono(String.class) //to specify response type
				.block();  //to make synchronous call

		if ("New Jersey".equals(stateName)) {

			// create application
			CitizenAppEntity entity = new CitizenAppEntity();
			BeanUtils.copyProperties(app, entity);
			entity.setStateName(stateName);
			citizenAppRepo.save(entity);
			return entity.getAppId();
		}

		return 0;
	}

}
