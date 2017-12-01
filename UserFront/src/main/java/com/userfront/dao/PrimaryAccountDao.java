package com.userfront.dao;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Integer>{
	PrimaryAccount findByAccountNumber(int accountNumber);
}
