package com.kitri.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * JPA : User Persistance Logic
 * @author msj0319
 * */
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	List<UserEntity> findByName(String name);
}
