package com.snort.app.service;

import java.util.List;

import com.snort.app.binding.ActivateAccount;
import com.snort.app.binding.Login;
import com.snort.app.binding.User;

public interface UserMgmtService {

	public boolean saveUser(User user);

	public boolean activateUserAcc(ActivateAccount activateAccount);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public User getUserByEmail(String email);

	public boolean deleteUserById(Integer userId);

	public boolean changeAccountStatus(Integer userId, String accStatus);

	public String login(Login login);

	public String forgotPwd(String email);

}
