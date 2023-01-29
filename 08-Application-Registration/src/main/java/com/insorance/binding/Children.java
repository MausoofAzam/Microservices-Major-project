package com.insorance.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Children {
	private Long caseNum;
	private Integer childId;
	private String childName;
	private LocalDate childAge;

	private Long childSsn;
}
