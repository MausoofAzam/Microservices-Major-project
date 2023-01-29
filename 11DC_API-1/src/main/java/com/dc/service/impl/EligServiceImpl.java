package com.dc.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dc.entity.CitizenAppEntity;
import com.dc.entity.CoTriggerEntity;
import com.dc.entity.DcCaseEntity;
import com.dc.entity.DcChildrenEntity;
import com.dc.entity.DcEducationEntity;
import com.dc.entity.DcIncomeEntity;
import com.dc.entity.EligDetailsEntity;
import com.dc.entity.PlanEntity;
import com.dc.repository.CitizenAppRepository;
import com.dc.repository.CoTriggerRepository;
import com.dc.repository.DcCaseRepo;
import com.dc.repository.DcChildrenRepo;
import com.dc.repository.DcEducationRepo;
import com.dc.repository.DcIncomeRepo;
import com.dc.repository.EligDtlsRepository;
import com.dc.repository.PlanRepository;
import com.dc.responce.EligResponse;
import com.dc.service.EligService;

@Service
public class EligServiceImpl implements EligService {

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private DcChildrenRepo childrenRepo;

	@Autowired
	private CitizenAppRepository citiAppRepository;

	@Autowired
	private DcEducationRepo dcEducationRepo;

	@Autowired
	private EligDtlsRepository eligDtlsRepository;

	@Autowired
	private CoTriggerRepository triggerRepository;

	@Override
	public EligResponse determineEligibility(Long caseNum) {
		// first get the plan id from case number
		Optional<DcCaseEntity> caseEntity = dcCaseRepo.findById(caseNum);
		Integer planId = null;
		String planName = null;
		Integer appId = null;

		if (caseEntity.isPresent()) {
			DcCaseEntity dcCaseEntity = caseEntity.get();
			planId = dcCaseEntity.getPlanId();
			appId = dcCaseEntity.getAppId();
		}
		// after getting case number find the plan name from plan id
		Optional<PlanEntity> planEntity = planRepository.findById(planId);

		if (planEntity.isPresent()) {
			PlanEntity plan = planEntity.get();
			planName = plan.getPlanName();
		}
		Optional<CitizenAppEntity> app = citiAppRepository.findById(appId);

		Integer age = 0;
		CitizenAppEntity citizenAppEntity = null;
		// based on the case number app id is get , and from app id we get age
		if (app.isPresent()) {
			citizenAppEntity = app.get();
			LocalDate dob = citizenAppEntity.getDob();
			LocalDate now = LocalDate.now();

			age = Period.between(dob, now).getYears();
		}
		EligResponse eligResponse = executePlanConditions(caseNum, planName, age);
		// logic to store data in database
		EligDetailsEntity eligEntity = new EligDetailsEntity();

		BeanUtils.copyProperties(eligResponse, eligEntity);
		eligEntity.setCaseNum(caseNum);
		eligEntity.setHolderName(citizenAppEntity.getFullName());
		eligEntity.setHolderSsn(citizenAppEntity.getSsn());

		eligDtlsRepository.save(eligEntity);
		CoTriggerEntity coTriggerEntity = new CoTriggerEntity();

		coTriggerEntity.setCaseNum(caseNum);
		coTriggerEntity.setTrgStatus("Pending");

		triggerRepository.save(coTriggerEntity);

		return eligResponse;

	}

	// outside class will not call this method so i have taken private

	private EligResponse executePlanConditions(Long caseNum, String planName, Integer age) {
		EligResponse response = new EligResponse();
	response.setPlanName(planName);
		// condition: if employment income <=300$ then citizen is eligible for snap.
		DcIncomeEntity income = incomeRepo.findByCaseNum(caseNum);
		// logic to execute condition
		
		//problems here not able to take plan name
		if ("SNAP".equals(planName)) {

			Double empIncome = income.getEmpIncome();

			if (empIncome <= 300) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("Hign Income");
			}
			// condition: if employmentIncome <=300$ and kidscount >0 and each kid age is
			// <=16 then eligible for ccap
		} else if ("CCAP".equals(planName)) {
			List<DcChildrenEntity> childs = childrenRepo.findByCaseNum(caseNum);

			boolean ageCondition = true;
			boolean kidsCountCondition = false;

			if (!childs.isEmpty()) {
				kidsCountCondition = true;

				for (DcChildrenEntity entity : childs) {

					Integer childAge = entity.getChildAge();

					if (childAge > 16) {
						ageCondition = false;
						break;
					}
				}
			}
			if (income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) {

				response.setPlanStatus("AP");

			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("Not Satisfied Business Rules");
			}

			// if employment income <=300 and propertyincome is 0, then eligible for
			// medicaid.
		} else if ("Medicaid".equals(planName)) {

			Double empIncome = income.getEmpIncome();
			Double propertyIncome = income.getPropertyIncome();

			if (empIncome <= 300 && propertyIncome == 0) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("High Income");
			}
			// if citizen age is >=65
		} else if ("Medicare".equals(planName)) {

			if (age >= 65) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");

				response.setPlanStatus("Age not Matched");
			}

			// if citizen is un-employed and graduated then eligible for NJW(New Jersey
			// Work)
		} else if ("NJW".equals(planName)) {

			DcEducationEntity educationEntity = dcEducationRepo.findByCaseNum(caseNum);
			Integer graduationYear = educationEntity.getGraduationYear();

			int currYear = LocalDate.now().getYear();
			if (income.getEmpIncome() <= 0 && graduationYear < currYear) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");

				response.setPlanStatus("Rules Not Satisfied");
			}
		}
		if (response.getPlanStatus().equals("AP")) {
			response.setPlanStartDt(LocalDate.now());
			response.setPlanEndDt(LocalDate.now().plusMonths(6));
			response.setBenefitAmt(350.00);
		}

		return response;
	}

}
