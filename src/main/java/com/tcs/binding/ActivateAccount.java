package com.tcs.binding;

import lombok.Data;

@Data
public class ActivateAccount {
	
	private String email;
	private String tmpPwd;
	private String newPwd;
	private String confirmPwd;

}
