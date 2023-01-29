package com.snort.app.binding;

import lombok.Data;

@Data
public class Login {

	private String email;
	
	private String password; // password is sensitive data so we have created login as separate binding class
}
