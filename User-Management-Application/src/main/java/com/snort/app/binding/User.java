package com.snort.app.binding;

import java.time.LocalDate;

import lombok.Data;

@Data

public class User {
	
	
	private String fullName;
	private String email;
	private Long mobile;
	private String gender;
	private LocalDate dob;
	private Long ssn;

}
