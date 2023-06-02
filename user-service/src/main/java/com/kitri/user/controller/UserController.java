package com.kitri.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kitri.user.dao.UserEntity;
import com.kitri.user.dto.UserDto;
import com.kitri.user.service.UserService;
import com.kitri.user.vo.RequestUser;
import com.kitri.user.vo.ResponseUser;

@RestController
@RequestMapping("/users")
public class UserController {
	
	UserService userService;

//  interface 구현 객체가 여러 개 일때
//	@Autowired
//	public UserController(@Qualifier("impl") UserService userService) {
//		super();
//		this.userService = userService;
//	}
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
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
	
}
