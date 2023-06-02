package com.kitri.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kitri.user.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
	
//	public static final Logger log = LoggerFactory.getLogger(JWTServiceImpl.class);

//	SALT는 토큰 유효성 확인 시 사용하기 때문에 외부에 노출되지 않게 주의해야 한다.
//	@Value("${token.salt}")
//	Environment env;
//	
//	@Autowired
//	public JWTServiceImpl(Environment env) {
//		this.env = env;
//	}
//	
//	private final String SALT = env.getProperty("token.salt");
//	
	private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 1; // 분단위
	private static final int REFRESH_TOKEN_EXPIRE_MINUTES = 2; // 주단위

	
	@Override
	public <T> String createAccessToken(String key, T data) {
		return create(key, data, "access-token", 1000 * 60 * ACCESS_TOKEN_EXPIRE_MINUTES);
	}
	//액세스 토큰이 만료되었을 때 다시 토큰을 생성하기 위한 토큰
	@Override
	public <T> String createRefreshToken(String key, T data) {
		return create(key, data, "refresh-token", 1000 * 60 * 60 * 24 * 7 * REFRESH_TOKEN_EXPIRE_MINUTES);
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
	private <T> String create(String key, T data, String subject, long expire) {
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
	
	
	
	// Signature 설정에 들어갈 key 생성.
	private byte[] generateKey(String salt) {
		byte[] key = null;
		try {
			// charset 설정 안하면 사용자 플랫폼의 기본 인코딩 설정으로 인코딩 됨.
			key = salt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (log.isInfoEnabled()) {
				e.printStackTrace();
			} else {
				log.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}

		return key;
	}

//	전달 받은 토큰이 제대로 생성된것인지 확인 하고 문제가 있다면 UnauthorizedException을 발생.
	@Override
	public boolean checkToken(String jwt, String key) {
		try {
//			Json Web Signature? 서버에서 인증을 근거로 인증정보를 서버의 private key로 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기
			Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey(key)).parseClaimsJws(jwt);
//			Claims 는 Map의 구현체 형태
			log.debug("claims: {}", claims);
			return true;
		} catch (Exception e) {
//			if (logger.isInfoEnabled()) {
//				e.printStackTrace();
//			} else {
			log.error(e.getMessage());
//			}
//			throw new UnauthorizedException();
//			개발환경
			return false;
		}
	}

	@Override
	public Map<String, Object> get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String jwt = request.getHeader("access-token");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(key.getBytes("UTF-8")).parseClaimsJws(jwt);
		} catch (Exception e) {
//			if (logger.isInfoEnabled()) {
//				e.printStackTrace();
//			} else {
			log.error(e.getMessage());
//			}
			throw new UnAuthorizedException();
//			개발환경
//			Map<String,Object> testMap = new HashMap<>();
//			testMap.put("userid", userid);
//			return testMap;
		}
		Map<String, Object> value = claims.getBody();
		log.info("value : {}", value);
		return value;


	}

	@Override
	public String getUserId() {
		return (String) this.get("user").get("userId");
	}

}
