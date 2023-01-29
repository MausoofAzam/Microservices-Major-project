package com.insorance.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CitizenApp {

	private String fullName;

	private String email;

	private Long phno;

	private String gender;

	private LocalDate dob;

	private Long ssn;
}
