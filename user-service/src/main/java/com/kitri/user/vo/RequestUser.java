package com.kitri.user.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Context Object : request data mapping
 * 					validate framework 이용 data valid check
 * @author msj0319
 * */

@Data
public class RequestUser {
	@NotNull(message="Email cannot be null")
	@Email
	private String email;
	
	private String name;
	
	@NotNull(message="Password cannot be null")
	@Size(min=8, message="password must be equal or grater then 8 character")
	private String password;
}
