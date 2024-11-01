package com.BSA.controllers;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BSA.services.AWSService;
import com.BSA.services.BankStatementService;
import com.opencsv.exceptions.CsvException;

@RestController
@RequestMapping("/statements")
public class BankStatementController {

	@Autowired
	private BankStatementService bankStatementService;

	@Autowired
	private AWSService awsService;

	@PostMapping("/generate")
	public String generateStatement(@RequestParam Long userId, @RequestParam Long companyId,
			@RequestParam Long branchId, @RequestParam int transactionCount, @RequestParam boolean deleteAfterUpload) {
		try {
			return bankStatementService.generateBankStatement(userId, companyId, branchId, transactionCount,
					deleteAfterUpload);
		} catch (IOException e) {
			return "Error generating bank statement: " + e.getMessage();
		}
	}

	@GetMapping("/download")
	public String downloadStatement(@RequestParam String key) {
		try {
			awsService.downloadFileToLocal(key);
			return "File downloaded successfully to your Downloads folder.";
		} catch (IOException e) {
			return "Error downloading bank statement: " + e.getMessage();
		}
	}

	@PostMapping("/parse")
	public String parseStatement(@RequestParam String filePath) throws ParseException {
		try {
			bankStatementService.parseAndSaveTransactions(filePath);
			return "Transactions parsed and saved successfully!";
		} catch (IOException | CsvException e) {
			return "Error parsing transactions: " + e.getMessage();
		}
	}
}
