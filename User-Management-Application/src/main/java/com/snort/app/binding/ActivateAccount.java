package com.snort.app.binding;

import lombok.Data;

@Data
public class ActivateAccount {
	//binding class represents request data and response data.
	private String fullName;
	
	private String email;

	private String tempPwd;

	private String newPwd;

	private String confirmPwd;
}
