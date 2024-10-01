package com.tcs.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String fullname;
	
	@Column(unique = true)
	private String email;
	private String password;
	private Character gender;
	private Long mobile;
	private Long ssn;
	private Boolean activeUser;
	private LocalDate dob;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(insertable = false)
	@UpdateTimestamp
	private LocalDate updatedDate;
	private String updatedBy;
	private String createdBy;

}
