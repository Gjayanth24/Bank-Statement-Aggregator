package com.BSA.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "bank_statements")
public class BankStatement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long statementId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date statementDate;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String statementData;

	public BankStatement() {
	}

	public BankStatement(Long statementId, User user, Company company, Branch branch, Date statementDate,
			String statementData) {
		this.statementId = statementId;
		this.user = user;
		this.company = company;
		this.branch = branch;
		this.statementDate = statementDate;
		this.statementData = statementData;
	}

	public Long getStatementId() {
		return statementId;
	}

	public void setStatementId(Long statementId) {
		this.statementId = statementId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Date getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(Date statementDate) {
		this.statementDate = statementDate;
	}

	public String getStatementData() {
		return statementData;
	}

	public void setStatementData(String statementData) {
		this.statementData = statementData;
	}

}
