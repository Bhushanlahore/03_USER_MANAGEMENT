package com.tcs.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.tcs.binding.ActivateAccount;
import com.tcs.binding.Login;
import com.tcs.binding.User;
import com.tcs.entity.UserMaster;
import com.tcs.repository.UserRepository;
import com.tcs.service.UserService;
import com.tcs.utils.EmailUtils;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	EmailUtils emailUtils;

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		List<UserMaster> entities = userRepo.findAll();

		List<User> userList = new ArrayList<>();

		for (UserMaster entity : entities) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			userList.add(user);
		}

		return userList;
	}

	@Override
	public UserMaster registerUser(User u) {
		// TODO Auto-generated method stub
		UserMaster entity = new UserMaster();

		entity.setEmail(u.getEmail().toLowerCase());
		entity.setPassword(generateRandomString());
		entity.setActiveUser(false);

		BeanUtils.copyProperties(u, entity);

		UserMaster save = userRepo.save(entity);

		String subject = "Your Registration Successfull !!!";
		

		String fileName = "REG-EMAIL-BODY.txt";

		String body = sendMailBody(entity.getFullname(), entity.getPassword(), fileName);

		emailUtils.sendMail(entity.getEmail(), subject, body);

		if (save.getUserId() != null) {
			return entity;
		}
		return null;
	}

	@Override
	public UserMaster updateUser(User u) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String forgetPwd(String email) {
		// TODO Auto-generated method stub

		UserMaster master = getUserByEmail(email);

		if (master == null) {

			return "please enter the valid email id";
		}

		String subject = "FORGET PASSWORD";
		String fileName = "FORGRT_MAIL.txt";

		String body = sendMailBody(master.getFullname(), master.getPassword(), fileName);

		emailUtils.sendMail(master.getEmail(), subject, body);

		return "Mail send successfully !!!";

	}

	@Override
	public boolean activateAcc(ActivateAccount acc) {
		// TODO Auto-generated method stub
		// my method
		UserMaster master = getUserByEmail(acc.getEmail());
		// String pwd = master.getPassword();
		if (master.getPassword().equals(acc.getTmpPwd())) {

			master.setPassword(acc.getNewPwd());
			master.setActiveUser(true);
			
			userRepo.save(master);

			return true;
		}

		return false;

		/*
		 * 
		 * UserMaster userMaster = new UserMaster();
		 * userMaster.setEmail(acc.getEmail()); userMaster.setPassword(acc.getTmpPwd());
		 * 
		 * Example<UserMaster> of = Example.of(userMaster);
		 * 
		 * List<UserMaster> masters = userRepo.findAll(of);
		 * 
		 * if(masters.isEmpty()) { return false; }else { UserMaster master =
		 * masters.get(0); master.setPassword(acc.getNewPwd());
		 * master.setActiveUser(true); return true; }
		 */
	}

	@Override
	public String login(Login login) {
		// TODO Auto-generated method stub

		// Method 1
		/*
		 * UserMaster userMaster = getUserByEmail(login.getEmail());
		 * 
		 * if (userMaster != null) { if
		 * ((userMaster.getEmail().equalsIgnoreCase(login.getEmail())) &&
		 * (userMaster.getPassword().equals(login.getPwd()))) { if
		 * (userMaster.getActiveUser().equals(true)) { return "login successfully"; }
		 * else {
		 * 
		 * return "Account is not active"; }
		 * 
		 * } else {
		 * 
		 * return "invalid email or password"; }
		 * 
		 * } else {
		 * 
		 * return "User not found with this email"; }
		 */

		// Method 2
		/*
		 * UserMaster userMaster = userRepo.findByEmailAndPassword(login.getEmail(),
		 * login.getPwd()); if(userMaster==null) { return "invalid credentials"; }
		 * 
		 * if(userMaster.getActiveUser().equals(true)) { return "login successful!!!":
		 * }else {
		 * 
		 * return "account is not active"; }
		 */

		UserMaster userMaster = new UserMaster();
		userMaster.setEmail(login.getEmail());
		userMaster.setPassword(login.getPwd());

		Example<UserMaster> of = Example.of(userMaster);

		List<UserMaster> entities = userRepo.findAll(of);

		if (entities.isEmpty()) {
			return "invalid credentials";
		} else {
			UserMaster master = entities.get(0);
			if (master.getActiveUser().equals(true)) {
				return "login successful!!!";
			} else {

				return "account is not active";
			}
		}
	}

	@Override
	public User getUserById(Integer userId) {
		// TODO Auto-generated method stub

		Optional<UserMaster> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}

		return null;
	}


	private UserMaster getUserByEmail(String email) {
		// TODO Auto-generated method stub
		UserMaster master = userRepo.findByEmail(email);

		return master;
	}

	@Override
	public boolean deleteUserByuserId(Integer userId) {
		// TODO Auto-generated method stub

		boolean status = false;

		try {
			userRepo.deleteById(userId);
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, boolean status) {
		// TODO Auto-generated method stub

		Optional<UserMaster> byId = userRepo.findById(userId);

		if (byId.isPresent()) {

			UserMaster master = byId.get();
			master.setActiveUser(status);
			userRepo.save(master);
			return true;
		}

		return false;
	}

	private String generateRandomString() {

		int length = 10;
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();

		return random.ints(length, 0, ALPHABET.length()).mapToObj(ALPHABET::charAt).map(Object::toString)
				.collect(Collectors.joining());
	}

	private String sendMailBody(String fullname, String pwd, String fileName) {

		String url = "http://localhost:8080/user-api/activate-acc";
		String mailBody = null;

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			StringBuffer sb = new StringBuffer();

			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();

			mailBody = sb.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullname);
			mailBody = mailBody.replace("{TMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", pwd);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return mailBody;
	}

}
