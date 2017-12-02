package com.userfront.service.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {

	private static int nextAccountNumber = 11223145;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());
		
		primaryAccountDao.save(primaryAccount);
		
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}
	
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());
		
		savingsAccountDao.save(savingsAccount);
		
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}
	
	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if ("Primary".equalsIgnoreCase(accountType)) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			
			PrimaryTransaction primaryTransaction = new PrimaryTransaction();
			primaryTransaction.setDate(date);
			primaryTransaction.setDescription("Deposit to Primary Account");
			primaryTransaction.setType("Account");
			primaryTransaction.setStatus("Finished");
			primaryTransaction.setAmount(amount);
			primaryTransaction.setAvailableBalance(primaryAccount.getAccountBalance());
			primaryTransaction.setPrimaryAccount(primaryAccount);
			
			transactionService.savePrimaryDepositTransaction(primaryTransaction);
			
		} else if ("savings".equalsIgnoreCase(accountType)) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			
			Date date = new Date();
			
			SavingsTransaction savingsTransaction = new SavingsTransaction();
			savingsTransaction.setDate(date);
			savingsTransaction.setDescription("Deposit to Savings Account");
			savingsTransaction.setType("Account");
			savingsTransaction.setStatus("Finished");
			savingsTransaction.setAmount(amount);
			savingsTransaction.setAvailableBalance(savingsAccount.getAccountBalance());
			savingsTransaction.setSavingsAccount(savingsAccount);
			
			transactionService.saveSavingsDepositTransaction(savingsTransaction);
		}
		
	}
	
	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		// TODO: Add some business logic to control amounts on withdraws. For instance: avoid amount > balance 
		
		if ("Primary".equalsIgnoreCase(accountType)) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			
			PrimaryTransaction primaryTransaction = new PrimaryTransaction();
			primaryTransaction.setDate(date);
			primaryTransaction.setDescription("Withdraw to Primary Account");
			primaryTransaction.setType("Account");
			primaryTransaction.setStatus("Finished");
			primaryTransaction.setAmount(amount);
			primaryTransaction.setAvailableBalance(primaryAccount.getAccountBalance());
			primaryTransaction.setPrimaryAccount(primaryAccount);
			
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
			
		} else if ("savings".equalsIgnoreCase(accountType)) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			
			Date date = new Date();
			
			SavingsTransaction savingsTransaction = new SavingsTransaction();
			savingsTransaction.setDate(date);
			savingsTransaction.setDescription("Withdraw to Savings Account");
			savingsTransaction.setType("Account");
			savingsTransaction.setStatus("Finished");
			savingsTransaction.setAmount(amount);
			savingsTransaction.setAvailableBalance(savingsAccount.getAccountBalance());
			savingsTransaction.setSavingsAccount(savingsAccount);
			
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		}
		
	}
	private static int accountGen() {
		return ++nextAccountNumber;
	}


}
