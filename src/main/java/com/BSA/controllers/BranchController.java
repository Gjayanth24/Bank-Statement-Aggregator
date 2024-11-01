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

import com.BSA.models.Branch;
import com.BSA.services.BranchService;

@RestController
@RequestMapping("/branches")
public class BranchController {

	@Autowired
	private BranchService branchService;

	@PostMapping("/create")
	public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
		try {
			Branch createdBranch = branchService.createBranch(branch);
			return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{branchId}")
	public ResponseEntity<?> getByBranchId(@PathVariable Long branchId) {
		Branch branch = branchService.getBranchById(branchId);
		if (branch != null) {
			return ResponseEntity.status(HttpStatus.OK).body(branch);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch Not Found");
	}

	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<Branch>> getBranchesByCompanyId(@PathVariable Long companyId) {
		List<Branch> branches = branchService.getBranchesByCompanyId(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(branches);
	}

}
