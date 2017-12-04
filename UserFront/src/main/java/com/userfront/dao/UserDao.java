package com.userfront.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.userfront.domain.User;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userDao
//CRUD refers Create, Read, Update, Delete

public interface UserDao extends CrudRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
	List<User> findAll();
}
