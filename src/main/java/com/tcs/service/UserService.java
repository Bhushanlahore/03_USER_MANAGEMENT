package com.tcs.service;

import java.util.List;

import com.tcs.binding.ActivateAccount;
import com.tcs.binding.Login;
import com.tcs.binding.User;
import com.tcs.entity.UserMaster;

public interface UserService {
	
	List<User> getAllUsers();
	
	UserMaster registerUser(User u);
	
	UserMaster updateUser(User u);
	
	String forgetPwd(String email);
	
	boolean activateAcc(ActivateAccount acc);
	
	String login(Login login);
	
	User getUserById(Integer userId);
	
	//UserMaster getUserByEmail(String email);
	
	boolean deleteUserByuserId(Integer userId);
	
	boolean changeAccountStatus(Integer userId, boolean status);

}
