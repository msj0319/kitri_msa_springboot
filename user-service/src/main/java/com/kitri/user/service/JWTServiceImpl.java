package com.kitri.user.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {

	@Override
	public <T> String createAccessToken(String key, T data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> String createRefreshToken(String key, T data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Token 발급
	 * @param <T>
	 * @param key : claim에 세팅할 key
	 * @param data : claim에 세팅할 data
	 * @param subject : payload에 sub value로 들어갈 값
	 * @param expire : 토큰 유효기간
	 * @return jwt : header + payload + signature
	 * */
	private <T> String createToken(String key, T data, String subject, long expire) {
		//payload 설정
		//subject, data, expire claim 세팅
		Claims claims = Jwts.claims()
						.setSubject(subject) //token subject
						.setExpiration(new Date(System.currentTimeMillis() + expire)); //token expire
		
		claims.put(key, data); //data 세팅
		
		String jwt = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
		
		return jwt;
	}

	@Override
	public boolean checkToken(String jwt) {
		// TODO Auto-generated method stub
		return false;
	}

}
