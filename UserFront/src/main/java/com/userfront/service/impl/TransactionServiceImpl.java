package com.userfront.service.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.RecipientDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;
	
	public List<PrimaryTransaction> findPrimaryTransactionList(String username){
        User user = userService.findByUsername(username);
        List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

        return primaryTransactionList;
    }

    public List<SavingsTransaction> findSavingsTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

        return savingsTransactionList;
    }

    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }
    
    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }
    
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		
		// FROM Primary TO Savings
        if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction();
            primaryTransaction.setDate(date);
            primaryTransaction.setDescription("Between account transfer from "+transferFrom+" to "+transferTo);
            primaryTransaction.setType("Account");
            primaryTransaction.setStatus("Finished");
            primaryTransaction.setAmount(Double.parseDouble(amount));
            primaryTransaction.setAvailableBalance(primaryAccount.getAccountBalance());
            primaryTransaction.setPrimaryAccount(primaryAccount);
            
            primaryTransactionDao.save(primaryTransaction);
            
            // TODO: Create also a SavingsTransaction entry to inform the transfer has been received
            return;
        } 
        
        // FROM Savings TO Primary
        if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction();
            savingsTransaction.setDate(date);
            savingsTransaction.setDescription("Between account transfer from "+transferFrom+" to "+transferTo);
            savingsTransaction.setType("Transfer");
            savingsTransaction.setStatus("Finished");
            savingsTransaction.setAmount(Double.parseDouble(amount));
            savingsTransaction.setAvailableBalance(savingsAccount.getAccountBalance());
            savingsTransaction.setSavingsAccount(savingsAccount);
            
            savingsTransactionDao.save(savingsTransaction);
            
            // TODO: Create also a PrimaryTransaction entry to inform the transfer has been received
            return;
        }
        
        throw new Exception("Invalid Transfer");
    }

	//-----------------------------------------------
	//  Recipients CRUD
	//-----------------------------------------------

	@Override
	public List<Recipient> findRecipientList(Principal principal) {
		String username = principal.getName();
		
		// TODO: create a custom method to get the filtered recipients. The actual solution is very inefficient
		List<Recipient> recipientList = recipientDao.findAll().stream()		// converts list to stream
				.filter(recipient -> username.equals(recipient.getUser().getUsername()))	// filters the line, 
				.collect(Collectors.toList());
		
		return recipientList;
	}

	
	@Override
	public void saveRecipient(Recipient recipient) {
		recipientDao.save(recipient);
		
	}

	@Override
	public Recipient findRecipientByName(String recipientName) {
		Recipient recipient = recipientDao.findByName(recipientName);
		return recipient;
	}

	@Override
	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);
	}
   
}
