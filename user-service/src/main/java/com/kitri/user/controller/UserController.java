package com.kitri.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kitri.user.dto.UserDto;
import com.kitri.user.exception.UnAuthorizedException;
import com.kitri.user.service.JWTService;
import com.kitri.user.service.UserService;
import com.kitri.user.vo.RequestUser;
import com.kitri.user.vo.ResponseUser;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
//@CrossOrigin("/**") //모든 요청에 대해 다 받을 수 있음
public class UserController {
	
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	UserService userService;
	JWTService jwtService;

//  interface 구현 객체가 여러 개 일때
//	@Autowired
//	public UserController(@Qualifier("impl") UserService userService) {
//		super();
//		this.userService = userService;
//	}
	
	@Autowired
	public UserController(UserService userService, JWTService jwtService) {
		this.userService = userService;
		this.jwtService = jwtService;
	}
	
	/**
	 * 사용자 등록요청 처리
	 * @param user : 사용자 정보
	 */
	@PostMapping("")
	public ResponseEntity<ResponseUser> regist(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		//request로 전달된 RequestUser -> UserDto 변환
		UserDto userDto = mapper.map(user, UserDto.class);
		//service 호출
		userDto = userService.regist(userDto);
		//service 리턴한 UserDto -> ResponseUser 변환
		ResponseUser resultUser = mapper.map(userDto, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resultUser);
	}
	
	/**
	 * 사용자 목록 조회
	 * @return List<ResponseUser> users
	 */
	@GetMapping("")
	public ResponseEntity<List<ResponseUser>> getUsers() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<ResponseUser> users = new ArrayList<ResponseUser>(); 
		List<UserDto> list = userService.getUsers();
		
		list.forEach(user -> {
				users.add(mapper.map(user, ResponseUser.class)); 
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	/**
	 * 사용자 정보 검색
	 * @param userId : 검색할 사용자 아이디
	 * @return User user
	 */
	@GetMapping("{userId}")
	public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = userService.getUser(userId);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(mapper.map(userDto, ResponseUser.class));
	}
	
	/**
	 * 사용자 정보 수정
	 * @param String userId
	 * @param RequestUser user
	 */
	@PutMapping("{userId}")
	public ResponseEntity<ResponseUser> modifyUser(@PathVariable String userId, 
						   @RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = userService.getUser(userId);
		
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		
		userDto = userService.modifyUser(userDto);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(mapper.map(userDto, ResponseUser.class)); 
	}
	
	/**
	 * 사용자 탈퇴
	 * @param String userId
	 */
	@DeleteMapping("{userId}")
	public ResponseEntity<String> removeUser(@PathVariable String userId) {
		String result = userService.removeUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
	@PostMapping("/login")
	//토큰 정보 json body로 확인하기 위한 반환 타입...........
	public ResponseEntity<Map<String, Object>> login(@RequestBody RequestUser user,
			HttpServletResponse response) {
		ModelMapper mapper = new ModelMapper();
		   mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		  //service email, password valid check
		 //valid한 경우 JWTService 이용 token 생성 리턴
		   Map<String, Object> resultMap = new HashMap<>();
		   String accessToken = null;
		   try {
			  UserDto userDto = userService.loginCheck(mapper.map(user, UserDto.class));
			  if (userDto != null) {
					accessToken = jwtService.createAccessToken("userId", userDto.getUserId());// key, data
					String refreshToken = jwtService.createRefreshToken("userId",userDto.getUserId());// key, data
					//refreshtoken redis memory db에 저장....
					log.debug("로그인 accessToken 정보 : {}", accessToken);
					log.debug("로그인 refreshToken 정보 : {}", refreshToken);
					resultMap.put("access-token", accessToken);
					resultMap.put("refresh-token", refreshToken);
					resultMap.put("message", SUCCESS);
					
					//응답 헤더에 accessToken 담기
					//response.addHeader("access-token", accessToken);
				}		  
		  
		  }catch(UnAuthorizedException loginError) {
			  exeptionHanler(loginError);
		  }
		   return ResponseEntity
				   .status(HttpStatus.ACCEPTED)
				   .header("access-token", accessToken)
				   .body(resultMap);

	}
	
	private ResponseEntity<String> exeptionHanler(Exception error) {
		   //exception type별로 처리
		   return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	   } 

	
}
