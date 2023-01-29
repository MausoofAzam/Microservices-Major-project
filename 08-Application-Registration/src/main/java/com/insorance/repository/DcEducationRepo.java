package com.insorance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insorance.entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Integer> {
	
	public DcEducationEntity findByCaseNum(Long caseNum);

}
