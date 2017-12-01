package com.userfront.dao;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Integer>{
	SavingsAccount findByAccountNumber(int accountNumber);
}
