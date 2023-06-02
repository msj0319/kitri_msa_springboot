package com.kitri.user.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kitri.user.exception.UnAuthorizedException;
import com.kitri.user.service.JWTService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {
	
	JWTService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//token check
		String token = request.getHeader("access-token");
		
		if(token != null && jwtService.checkToken(token, "userId")) {
			log.info("토큰 사용 가능 {}" + token);
			return true;
		} else {
			log.info("토큰 사용 불가능 {}" + token);
			throw new UnAuthorizedException();
		}
	}

}
