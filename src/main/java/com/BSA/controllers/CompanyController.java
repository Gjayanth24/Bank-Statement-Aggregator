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
import org.springframework.web.bind.annotation.RestController;

import com.BSA.models.Company;
import com.BSA.services.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@PostMapping("/create")
	public ResponseEntity<String> createCompany(@RequestBody Company company) {
		String result = companyService.createCompany(company);
		if (result.equals("Company created successfully"))
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		else
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
		Company company = companyService.getByCompanyId(id);
		if (company == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(company);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Company>> getAllCompanies() {
		List<Company> companies = companyService.getAllCompanies();
		return ResponseEntity.status(HttpStatus.OK).body(companies);
	}
}
