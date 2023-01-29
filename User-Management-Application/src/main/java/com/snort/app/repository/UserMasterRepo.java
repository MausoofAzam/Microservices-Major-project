package com.snort.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snort.app.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer>{
	
	public UserMaster findByEmailAndPassword(String email,String pwd);
	
	public UserMaster findByEmail(String email);
}


