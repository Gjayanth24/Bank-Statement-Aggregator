package com.BSA.models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "branch")
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long branchId;

	@Column(nullable = false)
	private String branchName;

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
//	@JsonBackReference
	private Company company;

	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<BankStatement> bankStatements;

	public Branch() {
	}

	public Branch(Long branchId, String branchName, Company company, Set<BankStatement> bankStatements) {
		this.branchId = branchId;
		this.branchName = branchName;
		this.company = company;
		this.bankStatements = bankStatements;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<BankStatement> getBankStatements() {
		return bankStatements;
	}

	public void setBankStatements(Set<BankStatement> bankStatements) {
		this.bankStatements = bankStatements;
	}

}
