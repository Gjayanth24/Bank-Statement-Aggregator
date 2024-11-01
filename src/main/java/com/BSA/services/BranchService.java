package com.BSA.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BSA.models.Branch;
import com.BSA.models.Company;
import com.BSA.repositories.BranchRepository;
import com.BSA.repositories.CompanyRepository;

@Service
public class BranchService {

	@Autowired
	private BranchRepository branchRepository;
	@Autowired
	private CompanyRepository companyRepository;

	public Branch createBranch(Branch branch) {
		Company company = companyRepository.findById(branch.getCompany().getCompanyId()) // get company id
				.orElseThrow(() -> new RuntimeException("Company not found"));
		branch.setCompany(company);
		return branchRepository.save(branch);
	}

	public Branch getBranchById(Long id) {
		return branchRepository.findById(id).orElse(null);
	}

	public List<Branch> getBranchesByCompanyId(Long companyId) {
		return branchRepository.findByCompanyCompanyId(companyId);
	}

}
