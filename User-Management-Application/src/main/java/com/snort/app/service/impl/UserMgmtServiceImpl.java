package com.snort.app.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.snort.app.binding.ActivateAccount;
import com.snort.app.binding.Login;
import com.snort.app.binding.User;
import com.snort.app.entity.UserMaster;
import com.snort.app.repository.UserMasterRepo;
import com.snort.app.service.UserMgmtService;
import com.snort.app.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {
		// here meaning of Activate account means status should be updated and password
		// should be updated
		UserMaster entity = new UserMaster();

		BeanUtils.copyProperties(user, entity);

		entity.setPassword(generateRandomPwd());
		// these two value are not coming from ui, we are setting
		entity.setAccountStatus("In-Active");

		UserMaster save = userMasterRepo.save(entity);

		String subject = "Your Registration Success";

		String filename = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(),filename);

		emailUtils.sendEmail(user.getEmail(), subject, body);
		return save.getUserId() != null;
	}

	/**
	 *
	 */
	@Override
	public boolean activateUserAcc(ActivateAccount activateAccount) {
		UserMaster entity = new UserMaster();
		entity.setEmail(activateAccount.getEmail());
		entity.setPassword(activateAccount.getTempPwd());

		// EXAMPLE WILL PREPARE THE QUERY FROM WHERE CLAUSE
		// I.E SELECT * FROM USERMASTER WHERE EMAIL=? AND PWD=?
		Example<UserMaster> of = Example.of(entity);

		List<UserMaster> findAll = userMasterRepo.findAll(of);
		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAccount.getNewPwd());
			userMaster.setAccountStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<UserMaster> findAll = userMasterRepo.findAll();
		List<User> users = new ArrayList<>();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, users);

			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		User user = new User();
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();

			BeanUtils.copyProperties(userMaster, user);
		}
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This is a single method from which activation and De-Activation can be
	 * performed.
	 */
	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccountStatus(accStatus);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	/**
	 * here in this method we are verifying that based on email id and password
	 * 
	 *
	 */
	@Override
	public String login(Login login) {
		// 1st Approach
//		UserMaster entity = new UserMaster();
//		entity.setEmail(login.getEmail());
//		entity.setPassword(login.getPassword());
//		//select * from From user_master where email=? and pwd=?
//		Example<UserMaster> of = Example.of(entity);
//		List<UserMaster> findAll = userMasterRepo.findAll();
//		if (findAll.isEmpty()) {
//			return "Invalid Credentials";
//		} else {
//			UserMaster userMaster = findAll.get(0);
//			if (userMaster.getAccountStatus().equals("Active")) {
//				return "SUCCESS";
//			} else {
//				return "Account Not Activated";
//			}
//	}
		UserMaster entity = userMasterRepo.findByEmailAndPassword(login.getEmail(), login.getPassword());
		if (entity == null) {
			return "Invalid Credentials";
		}
		if (entity.getAccountStatus().equals("Active")) {
			return "SUCCESS";
		} else {
			return "Account Not Activated";
		}
	}

	@Override
	public String forgotPwd(String email) {
		UserMaster entity = userMasterRepo.findByEmail(email);
		if (entity == null) {
			return "Invalid Email";
		}
		String subject ="Recovery Password";
		String fileName ="RECOVER-PWD-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		boolean sendEmail = emailUtils.sendEmail(fileName, subject, body);
		if(sendEmail) {
			return "Password sent to your Registered email";
		}
		return null;
	}

	private String generateRandomPwd() {

		// create a string of upper-case and lower-case characters and numbers
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		// combine all strings i.e AlphaNumeric
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
		// create random string builder
		StringBuilder sb = new StringBuilder();
		// create an object of Random class
		Random random = new Random();
		// specify length of random string
		int length = 6;
		for (int i = 0; i < length; i++) {
			// generate random index number
			int index = random.nextInt(alphaNumeric.length());
			// get character specified by index from the string
			char randomChar = alphaNumeric.charAt(index);
			// append the character to string builder
			sb.append(randomChar);
		}

		String randomPwd = sb.toString();
		System.out.println("Random String is: " + randomPwd);
		return randomPwd;
	}

	/**
	 * This is the logic which is used to read the file data line by line
	 * 
	 * @return
	 */
	private String readEmailBody(String fullName, String Pwd, String filename) {
		String url = "";
		String mailBody = null;
		// file Reader will read the data character by character
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer buffer = new StringBuffer();

			String line = br.readLine();
			while (line != null) {
				// process the data
				buffer.append(line);
				line = br.readLine();
			}
			br.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", Pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("PWD", Pwd);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return mailBody;

	}

}
