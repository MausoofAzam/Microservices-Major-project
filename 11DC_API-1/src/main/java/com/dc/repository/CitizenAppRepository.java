package com.dc.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.entity.CitizenAppEntity;

public interface CitizenAppRepository extends JpaRepository<CitizenAppEntity, Integer> {

}
