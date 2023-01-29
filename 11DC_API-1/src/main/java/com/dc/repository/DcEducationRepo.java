package com.dc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Integer> {
	
	public DcEducationEntity findByCaseNum(Long caseNum);

}
