package com.insorance.service;

import java.util.Map;

import com.insorance.binding.ChildRequest;
import com.insorance.binding.DcSummary;
import com.insorance.binding.Education;
import com.insorance.binding.Income;
import com.insorance.binding.PlanSelection;

public interface DcService {

	public Long loadCaseNum(Integer appId);

	public Map<Integer, String> getPlanNames();

	public Long savePlanSelection(PlanSelection planSelection);

	public Long saveIncomeData(Income income);

	public Long saveEducation(Education education);

	public Long saveChildren(ChildRequest request);

	public DcSummary getSummary(Long caseNumber);
	
}
