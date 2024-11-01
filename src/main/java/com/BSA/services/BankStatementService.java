package com.BSA.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BSA.models.BankStatement;
import com.BSA.models.Branch;
import com.BSA.models.Company;
import com.BSA.models.Transaction;
import com.BSA.models.User;
import com.BSA.repositories.BankStatementRepository;
import com.BSA.repositories.BranchRepository;
import com.BSA.repositories.CompanyRepository;
import com.BSA.repositories.TransactionRepository;
import com.BSA.repositories.UserRepository;
import com.BSA.utils.DummyDataGenerator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class BankStatementService {

	private static final String STATEMENTS_DIR = "C:/Users/jayan/Documents/workspace-sts/BankStatementAggregator/BankStatements";

	@Autowired
	private BankStatementRepository bankStatementRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private AWSService awsService;

	@Autowired
	private TransactionRepository transactionRepository;

	public String generateBankStatement(Long userId, Long companyId, Long branchId, int transactionCount,
			boolean deleteAfterUpload) throws IOException {
		User user = userRepository.findById(userId). //
				orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Company company = companyRepository.findById(companyId). //
				orElseThrow(() -> new IllegalArgumentException("Invalid company ID"));
		Branch branch = branchRepository.findById(branchId). //
				orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));

		List<Transaction> transactions = DummyDataGenerator.generateDummyTransactions(transactionCount,
				company.getCompanyName(), branch.getBranchName());

		// Use formatted date and time in the file name
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(formatter);
		String fileName = "company_" + companyId + "_user_" + userId + "_" + timestamp + ".csv";
		String filePath = STATEMENTS_DIR + "/" + fileName;

		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("Transaction_id,Date,Amount,Description,Company_name");
			for (Transaction transaction : transactions) {
				writer.printf("%s,%s,%.2f,%s,%s%n", transaction.getTransactionId(), //
						transaction.getDate(), //
						transaction.getAmount(), //
						transaction.getDescription(), //
						transaction.getCompanyName()); //
			}
		}

		String fileUrl = awsService.uploadFile(filePath, fileName);

		BankStatement bankStatement = new BankStatement();
		bankStatement.setUser(user);
		bankStatement.setCompany(company);
		bankStatement.setBranch(branch);
		bankStatement.setStatementDate(new Date());
		bankStatement.setStatementData(fileUrl);

		bankStatementRepository.save(bankStatement);

		// Delete temporary file after upload if flag is set
		if (deleteAfterUpload) {
			new File(filePath).delete();
		}

		return fileUrl;
	}

	/*
	 * This method converts the transactions of csv file into into java objects and
	 * then we can save that in the database
	 */
	public void parseAndSaveTransactions(String filePath) throws IOException, CsvException {
		// Ensure the file path is valid and accessible
		filePath = filePath.replace("\\", "/");
		System.out.println("Attempting to read file at: " + filePath);
		File csvFile = new File(filePath);
		if (!csvFile.exists() || !csvFile.canRead()) {
			throw new IOException("File does not exist or cannot be read: " + filePath);
		}

		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
			List<String[]> records = reader.readAll();
			// Check if there are records to process
			if (records.size() <= 1) {
				System.out.println("No records found in the file: " + filePath);
				return;
			}

			for (String[] record : records.subList(1, records.size())) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(record[0]);

				// Adjust date format based on CSV content
				String dateString = record[1];
				try {
					// Use a more flexible date parsing approach
					transaction.setDate(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateString));
				} catch (ParseException e) {
					System.out.println("Date parsing error at row: " + record[0] + ", date: " + dateString);
					e.printStackTrace();
					continue; // Skip this record if the date format is incorrect
				}

				// Parse the amount and handle potential parsing errors
				try {
					transaction.setAmount(Double.parseDouble(record[2]));
				} catch (NumberFormatException e) {
					System.out
							.println("Number format error for transaction ID: " + record[0] + ", amount: " + record[2]);
					e.printStackTrace();
					continue; // Skip this record if the amount is invalid
				}

				transaction.setDescription(record[3]);
				transaction.setCompanyName(record[4]);
				// Save the transaction object to the repository
				transactionRepository.save(transaction);
			}
		} catch (IOException e) {
			System.out.println("File read error: " + filePath);
			e.printStackTrace();
		} catch (CsvException e) {
			System.out.println("CSV format error: " + filePath);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
