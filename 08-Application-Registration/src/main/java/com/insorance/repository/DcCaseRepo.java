package com.insorance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insorance.entity.DcCaseEntity;

public interface DcCaseRepo extends JpaRepository<DcCaseEntity, Long> {

	//based on app id we get the case number
	public DcCaseEntity findByAppId(Integer appId);
}
