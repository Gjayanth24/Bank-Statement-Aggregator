package com.BSA.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BSA.models.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
	List<Branch> findByCompanyCompanyId(Long companyId);
}
