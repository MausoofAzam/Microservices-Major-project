package com.insorance.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "DC_EDUCATION")
public class DcEducationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eduId;
	private Long caseNum;
	private String highestQualification;
	private Integer graduationYear;
	private String universityName;

}
