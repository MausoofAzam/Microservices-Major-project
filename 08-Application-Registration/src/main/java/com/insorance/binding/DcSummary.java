package com.insorance.binding;

import java.util.List;

import lombok.Data;

@Data
public class DcSummary {

	
	private Income  income;
	
	private Education education;
	
	private String planName;
	
	private List<Children> childrens;
}
