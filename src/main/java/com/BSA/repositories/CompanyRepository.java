package com.BSA.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BSA.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	Optional<Company> findByCompanyName(String companyName);

}
