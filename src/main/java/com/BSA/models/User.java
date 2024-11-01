package com.BSA.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String userName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String password;

	/* Many users can be in one company */
	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	/*
	 * One user can have many bankstatements, cascade is for operations performed in
	 * user automatically reflect in bankstatemnt fetch is lazy i.e bankstatement
	 * not fetch until it uses
	 * 
	 * @JsonIgnore is it will ignore when user is displaying json format, Set is for
	 * not getting duplicates BankSt.
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<BankStatement> bankStatements;

	public User() {
	}

	public User(Long userId, String name, String userName, String email, String password, Company company,
			Set<BankStatement> bankStatements) {
		this.userId = userId;
		this.name = name;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.company = company;
		this.bankStatements = bankStatements;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
