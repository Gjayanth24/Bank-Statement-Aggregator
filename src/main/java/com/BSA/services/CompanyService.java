package com.BSA.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BSA.models.Company;
import com.BSA.repositories.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	public String createCompany(Company company) {
		Optional<Company> existingCompany = companyRepository.findByCompanyName(company.getCompanyName());
		if (existingCompany.isPresent()) {
			return "Company with name '" + company.getCompanyName() + "' already exists.";
		} else {
			companyRepository.save(company);
			return "Company created successfully";
		}
	}

	public Company getByCompanyId(Long id) {
		return companyRepository.findById(id).orElse(null);
	}

	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}
}
