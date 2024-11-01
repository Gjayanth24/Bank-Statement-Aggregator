package com.BSA.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.BSA.models.Transaction;

public class DummyDataGenerator {

//	private static final String ALPHABETIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//	private static final int TRANSACTION_ID_LENGTH = 8;
//	private static final Random RANDOM = new Random();

	public static List<Transaction> generateDummyTransactions(int count, String companyName, String branchName) {

		List<Transaction> transactions = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			transactions.add(new Transaction(generateTransactionId(), // Generate TransactionId
					new Date(), // current Date
					Math.random() * 1000, // random amount
					"Transaction from " + branchName + " branch", // Description
					companyName // get companyName
			));
		}
		return transactions;
	}

	private static String generateTransactionId() {

		return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

	}

}
