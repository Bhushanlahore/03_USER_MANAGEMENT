package com.tcs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.binding.ActivateAccount;
import com.tcs.binding.Login;
import com.tcs.binding.User;
import com.tcs.entity.UserMaster;
import com.tcs.service.UserService;

@RestController
@RequestMapping("/user-api")
public class UserController {
	
	@Autowired
	private UserService uService;
	
	@GetMapping("/getUsers")
	public ResponseEntity<List<User>> getAllUsers(){
		
	 	List<User> users= uService.getAllUsers();
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping("/saveUser")
	public ResponseEntity<UserMaster> registerUser(@RequestBody User u){
		
		 UserMaster registerUser = uService.registerUser(u);
		 
		 if (registerUser != null) {
			 return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
		}else {
			return new 	ResponseEntity<UserMaster>(registerUser, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 
		 
	}
	
	@GetMapping("/forget-pwd/{mail}")
	public ResponseEntity<String> forgetPassword(@PathVariable String mail){
	
		String pwd = uService.forgetPwd(mail);
		return new ResponseEntity<>(pwd, HttpStatus.OK);
	}
	
	@PostMapping("/activate-acc")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount acc){
		
		boolean isActivated = uService.activateAcc(acc);
		
		if(isActivated) {
			return new ResponseEntity<>("Account Activated", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@PostMapping("/user-login")
	public ResponseEntity<String> userLogin(@RequestBody Login login){
		
		String userLog = uService.login(login);
		
		return new ResponseEntity<String>(userLog, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUser(@PathVariable Integer userId){
		
		User userById = uService.getUserById(userId);
		
		if(userById != null) {
			return new ResponseEntity<>(userById, HttpStatus.OK);
		}else {
			
			return new ResponseEntity<>(userById, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
		
		boolean deleteUserByuserId = uService.deleteUserByuserId(userId);
		
		if(deleteUserByuserId) {
			return new ResponseEntity<String>("User is deleted", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("User is not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> ChangeStatus(@PathVariable Integer userId, @PathVariable boolean status){
		
		boolean isStatusChanged = uService.changeAccountStatus(userId, status);
		
		if(isStatusChanged) {
			return new ResponseEntity<>("Status is changed", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Status is not changed", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
