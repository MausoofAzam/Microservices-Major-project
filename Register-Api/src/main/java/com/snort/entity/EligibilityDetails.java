package com.snort.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class EligibilityDetails {

	@Id
	private Integer eligId; //genvalue not needed for retriving data
	private String name;
	private Long mobile;
	private String email;
	private Character gender;
	private Long ssn;
	private String planName;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndtDate;
	private LocalDate createDate;
	private LocalDate updatetDate;

	private String createdBy;
	private String updatedBy;

}
