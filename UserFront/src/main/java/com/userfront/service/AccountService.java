package com.userfront.service;

import java.security.Principal;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;

public interface AccountService {
	
	PrimaryAccount createPrimaryAccount();
	
	SavingsAccount createSavingsAccount();
	
	void deposit(String accountType, double amount, Principal principal);
	
	void withdraw(String accountType, double amount, Principal principal);
}
