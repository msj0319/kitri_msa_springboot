package com.kitri.user.service;

import java.util.List;

import com.kitri.user.dto.UserDto;

/**
 * Users Business Logic 서비스 발행
 * @author msj0319
 * */
public interface UserService {
	UserDto regist(UserDto user);
	UserDto getUser(String userId);
	List<UserDto> getUsers();
	UserDto modifyUser(UserDto user);
	String removeUser(String userId);
}
