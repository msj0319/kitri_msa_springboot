package com.kitri.user.exception;

public class UnAuthenticationException extends RuntimeException{
	public UnAuthenticationException() {
		super("다시 로그인해주세요");
	}
}
