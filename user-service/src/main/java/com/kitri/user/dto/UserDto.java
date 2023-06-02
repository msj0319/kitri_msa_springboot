package com.kitri.user.dto;

import lombok.Data;

/**
 * controller에서 service 통신하기 위한 data
 * @author msj0319
 * */
@Data
public class UserDto implements java.io.Serializable {
	private String userId;
	private String email;
	private String name;
	private String password;
	private String encryptedPassword;
}
