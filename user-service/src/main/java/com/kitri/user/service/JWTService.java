package com.kitri.user.service;

import java.util.Map;

public interface JWTService {
	<T> String createAccessToken(String key, T data);
	<T> String createRefreshToken(String key, T data);
	boolean checkToken(String jwt);
	Map<String, Object> get(String key);
	String getUserId();
}
