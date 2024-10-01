package com.tcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.entity.UserMaster;


@Repository
public interface UserRepository extends JpaRepository<UserMaster, Integer> {
	
	UserMaster findByEmail(String email);
	
	UserMaster findByEmailAndPassword(String email, String password);

}
