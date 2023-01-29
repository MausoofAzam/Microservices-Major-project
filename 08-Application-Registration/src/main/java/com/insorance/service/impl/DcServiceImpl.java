package com.insorance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insorance.binding.ChildRequest;
import com.insorance.binding.Children;
import com.insorance.binding.DcSummary;
import com.insorance.binding.Education;
import com.insorance.binding.Income;
import com.insorance.binding.PlanSelection;
import com.insorance.entity.CitizenAppEntity;
import com.insorance.entity.DcCaseEntity;
import com.insorance.entity.DcChildrenEntity;
import com.insorance.entity.DcEducationEntity;
import com.insorance.entity.DcIncomeEntity;
import com.insorance.entity.PlanEntity;
import com.insorance.repository.CitizenAppRepository;
import com.insorance.repository.DcCaseRepo;
import com.insorance.repository.DcChildrenRepo;
import com.insorance.repository.DcEducationRepo;
import com.insorance.repository.DcIncomeRepo;
import com.insorance.repository.PlanRepository;
import com.insorance.service.DcService;

@Service
public class DcServiceImpl implements DcService {

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private DcIncomeRepo dcIncomeRepo;

	@Autowired
	private DcEducationRepo educationRepo;

	@Autowired
	private DcChildrenRepo childrenRepo;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	/**
	 * based on the application id i am loading the case number
	 *
	 */
	@Override
	public Long loadCaseNum(Integer appId) {
		Optional<CitizenAppEntity> app = appRepo.findById(appId);

		if (app.isPresent()) {
			DcCaseEntity entity = new DcCaseEntity();
			entity.setAppId(appId);
			entity = dcCaseRepo.save(entity);

			return entity.getCaseNum();
		}
		return 0L;
	}

	/**
	 * This method is used to retrieve only plan names
	 *
	 */
	@Override
	public Map<Integer, String> getPlanNames() {

		List<PlanEntity> findAll = planRepository.findAll();

		Map<Integer, String> plansMap = new HashMap<>();

		for (PlanEntity entity : findAll) {
			plansMap.put(entity.getPlanId(), entity.getPlanName());
		}

		return plansMap;
	}

	@Override
	public Long savePlanSelection(PlanSelection planSelection) {
		Optional<DcCaseEntity> findById = dcCaseRepo.findById(planSelection.getCaseNum());

		if (findById.isPresent()) {
			DcCaseEntity dcCaseEntity = findById.get();
			dcCaseEntity.setPlanId(planSelection.getPlanId());

			dcCaseRepo.save(dcCaseEntity);

			return planSelection.getCaseNum();
		}
		return null;
	}

	@Override
	public Long saveIncomeData(Income income) {
		DcIncomeEntity entity = new DcIncomeEntity();
		BeanUtils.copyProperties(income, entity);
		dcIncomeRepo.save(entity);
		return income.getCaseNum();
	}

	@Override
	public Long saveEducation(Education education) {
		DcEducationEntity entity = new DcEducationEntity();
		BeanUtils.copyProperties(education, entity);

		educationRepo.save(entity);
		return education.getCaseNum();
	}

	@Override
	public Long saveChildren(ChildRequest request) {

		List<Children> childs = request.getChilds();
		
		Long caseNum = request.getCaseNum();
		
		
		for (Children c : childs) {
			DcChildrenEntity entity = new DcChildrenEntity();
			BeanUtils.copyProperties(c, entity);
			entity.setCaseNum(caseNum);
			childrenRepo.save(entity);

		}
		return request.getCaseNum();
	}

	@Override
	public DcSummary getSummary(Long caseNumber) {
		// setting the data from table
		String planName = "";
		DcIncomeEntity incomeEntity = incomeRepo.findByCaseNum(caseNumber);
		DcEducationEntity educationDataEntity = educationRepo.findByCaseNum(caseNumber);
		List<DcChildrenEntity> childsEntity = childrenRepo.findByCaseNum(caseNumber);
		Optional<DcCaseEntity> dcCase = dcCaseRepo.findById(caseNumber);

		if (dcCase.isPresent()) {
			Integer planId = dcCase.get().getPlanId();

			Optional<PlanEntity> plan = planRepository.findById(planId);
			if (plan.isPresent()) {
				planName = plan.get().getPlanName();
			}
		}
		// now set the data to summary object
		DcSummary summary = new DcSummary();
		summary.setPlanName(planName);

		Income income = new Income();
		BeanUtils.copyProperties(incomeEntity, income);

		Education education = new Education();
		BeanUtils.copyProperties(educationDataEntity, education);
		summary.setEducation(education);

		List<Children> childs = new ArrayList<>();

		for (DcChildrenEntity entity : childsEntity) {
			Children ch = new Children();
			BeanUtils.copyProperties(entity, ch);
			childs.add(ch);
		}
		return summary;
	}

}
