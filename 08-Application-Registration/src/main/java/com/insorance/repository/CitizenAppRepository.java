package com.insorance.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.insorance.entity.CitizenAppEntity;

public interface CitizenAppRepository extends JpaRepository<CitizenAppEntity, Integer> {

}
