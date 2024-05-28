package com.example.MyBank;

import com.example.MyBank.controller.BankController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MyBankApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MyBankApplication.class, args);

		BankController bankController = context.getBean(BankController.class);

	/*	try {
			System.out.println(bankController.getCashAccountBalance().toString());

		} catch (AccountBalanceNotFoundException e) {
			System.err.println("Error fetching account balance: " + e.getMessage());
		}*/
	}

}
