package com.dc.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.entity.EligDetailsEntity;

public interface EligDtlsRepository extends JpaRepository<EligDetailsEntity, Serializable> {

	public EligDetailsEntity findByCaseNum(Long caseNum);
}
