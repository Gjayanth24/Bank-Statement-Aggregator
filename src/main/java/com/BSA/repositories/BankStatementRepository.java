package com.BSA.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BSA.models.BankStatement;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {

	
}
