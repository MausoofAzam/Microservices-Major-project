package com.dc.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.entity.CoTriggerEntity;

public interface CoTriggerRepository extends JpaRepository<CoTriggerEntity, Serializable> {

	public List<CoTriggerEntity> findByTrgStatus(String status);

	public CoTriggerEntity findByCaseNum(Long caseNum);

}
