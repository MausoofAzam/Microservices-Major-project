package com.dc.service;

import com.dc.responce.EligResponse;

public interface EligService {
	
	public EligResponse determineEligibility(Long caseNum);

}
