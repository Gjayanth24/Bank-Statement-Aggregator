package com.BSA.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BSA.models.Company;
import com.BSA.models.User;
import com.BSA.repositories.CompanyRepository;
import com.BSA.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public String registerUser(User user) {
		if (user.getCompany() == null || user.getCompany().getCompanyId() == null) {
			return "Company information is required";
		}
		// checking if fields are empty
		if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()
				|| user.getUserName().isEmpty()) {
			return "Fields cannot be Empty";
		}

		// checking if user exits or not
		Company company = companyRepository.findById(user.getCompany().getCompanyId()).orElse(null);
		if (company == null) {
			return "Company not exits";
		}
		// check if already exits
		if (userRepository.findByUserName(user.getUserName()) != null
				|| userRepository.findByEmail(user.getEmail()) != null) {
			String message = "User with username '" + user.getUserName() + "' or email '" + user.getEmail()
					+ "' already exits";
			return message;
		}
		user.setCompany(company);
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return "Registered Successfully";
	}

	public String loginUser(String userName, String password) {
		User user = userRepository.findByUserName(userName);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return "User login successfully";
		} else {
			System.out.println("Invalid username or password for user: " + userName);
			return "Invalid username or password";
		}
	}

	public User getByUserId(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
