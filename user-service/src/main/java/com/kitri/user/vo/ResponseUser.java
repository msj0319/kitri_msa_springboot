package com.kitri.user.vo;

import lombok.Data;

/**
 * Context Object : response data mapping
 * 					validate framework 이용 data valid check
 * @author msj0319
 * */
@Data
public class ResponseUser {
	private String userId;
	private String email;
	private String name;
}
