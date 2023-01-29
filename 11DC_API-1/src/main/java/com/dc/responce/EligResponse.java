package com.dc.responce;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligResponse {

	private String planName;

	private String planStatus;

	private LocalDate planStartDt;

	private LocalDate planEndDt;

	private Double benefitAmt;

	private String denialReason;
}
