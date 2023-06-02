package com.kitri.user.exception;

public class UnAuthorizedException extends RuntimeException {
	public UnAuthorizedException() {
		super("권한이 없습니다. 인증 확인해주세요.");
	}

}
