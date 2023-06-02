package com.kitri.user.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kitri.user.interceptor.JWTInterceptor;

//JWTInterceptor 실행 시점 작성, 스프링 시큐리티 사용 안하는 경우 WebMvcConfigurer 인터페이스 구현체 작성
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	JWTInterceptor jwtInterceptor;
	
	private final List<String> patterns = Arrays.asList("/users/login", "/users"); 
	
	public WebMVCConfig(JWTInterceptor jwtInterceptor) {
		this.jwtInterceptor = jwtInterceptor;
	}
	
	//인터셉터 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).excludePathPatterns(patterns);
												//인터셉터를 제외할 url 패턴 등록
												//.addPathPatterns(patterns)
	}													
	//인터셉터에 대해 요청을 허용할 url 등록
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**");
//	}
	

}
