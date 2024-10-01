package com.tcs.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String fullname;
	private String email;
	private Character gender;
	private Long mobile;
	private Long ssn;
	private LocalDate dob;

}
