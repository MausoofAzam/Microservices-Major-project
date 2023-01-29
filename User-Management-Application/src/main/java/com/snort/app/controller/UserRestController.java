package com.snort.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snort.app.binding.ActivateAccount;
import com.snort.app.binding.Login;
import com.snort.app.binding.User;
import com.snort.app.service.UserMgmtService;

@RestController
public class UserRestController {

	@Autowired
	private UserMgmtService service;

	/**
	 * This is binded to @PostMapping("/user") method in which User object we are taking as
	 * a parameter This is used to save the user record, what ever the form data we
	 * are getting we are taking as a Request body to read data from request body we
	 * are using @Requestbody if the user is save successfully then we will display
	 * Registration success message otherwise registration failed message. This
	 * method is also used for updating
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user) {
		boolean saveUser = service.saveUser(user);
		if (saveUser) {
			return new ResponseEntity<String>("Registration Success", HttpStatus.CREATED);

		} else {
			return new ResponseEntity<String>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * if user has given correct Temporary password then only account will be
	 * activated.otherwise account will not be activated. when user submit this form
	 * we will get a request to this method, the form data will come in the request
	 * body,then we are calling activate user account if it is activated the it will
	 * display "Account Activated" will 200 status success, if account is not
	 * activated that means the user has given invalid temporary password then it
	 * will display "Invalid Temporary Password" with badRequest
	 * 
	 * @param acc
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount acc) {
		boolean isActivated = service.activateUserAcc(acc);

		if (isActivated) {
			return new ResponseEntity<String>("Account Activated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to retrieve the users to display in the table and display
	 * in the UI
	 * 
	 * @return
	 */
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = service.getAllUsers();

		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
	}

	/**
	 * Here we are getting the user record based on the id
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User user = service.getUserById(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/**
	 * here we are deleting the user record based on the userId if Record is delete
	 * then it will display deleted message as deleted with httpStatus 200 if not
	 * deleted then displayed message will be Failed with INTERNAL_SERVER_ERROR
	 * 
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = service.deleteUserById(userId);

		if (isDeleted) {
			return new ResponseEntity<String>("Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to change the status of user account, for this userId and
	 * status both in the request as path variable after communicating with service
	 * layer it will give some response isChanged. if isChanged successfully then it
	 * will display message in the ui as "Status Changed" with 200 Http-status
	 * otherwise it will display "Failed to Changed",
	 * HttpStatus.INTERNAL_SERVER_ERROR
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String status) {
		boolean isChanged = service.changeAccountStatus(userId, status);
		if (isChanged) {
			return new ResponseEntity<String>("Status Changed", HttpStatus.OK);
		} else {

			return new ResponseEntity<String>("Failed to Changed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * we should not send the data in the url so we are taking post method here,
	 * Login object we are taking as a parameter this method will talk to service
	 * layer in which login method is executed, and we are given response entity as
	 * status
	 * 
	 * @param login
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String status = service.login(login);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

	/**
	 * This method is used to get the recovery password based on the email of the
	 * user to taking email as a path parameter i am taking email as @PathVariable
	 * we will get password, what ever the message will generate we will response to
	 * ui. whatever the status we get based on the status success message or failure
	 * message will be displayed in the page.
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/forgotpwd/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email) {
		String status = service.forgotPwd(email);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
