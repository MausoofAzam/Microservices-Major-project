package com.insorance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insorance.entity.DcChildrenEntity;

public interface DcChildrenRepo extends JpaRepository<DcChildrenEntity, Integer> {

	public List<DcChildrenEntity> findByCaseNum(Long caseNum);
}
