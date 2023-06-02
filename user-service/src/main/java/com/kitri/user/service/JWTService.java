package com.kitri.user.service;

public interface JWTService {
	<T> String createAccessToken(String key, T data);
	<T> String createRefreshToken(String key, T data);
	boolean checkToken(String jwt);
}
