package com.BSA.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BSA.models.User;
import com.BSA.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		String result = userService.registerUser(user);
		if(result.equals("Fields cannot be Empty")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		if (result.equals("Company information is required")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} else if (result.equals("Company not exits")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		} else if (result.contains("already exits")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		} else if (result.equals("Registered Successfully")) {
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		}

		// Default response if none of the above cases match
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestParam String userName, String password) {
		String result = userService.loginUser(userName, password);
		if (result.equals("User login successfully")) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getByUserId(@PathVariable Long id) {
		User user = userService.getByUserId(id);
		if (user != null)
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(users);
	}

}
