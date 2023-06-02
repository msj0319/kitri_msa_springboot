package com.kitri.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitri.user.dao.UserEntity;
import com.kitri.user.dao.UserRepository;
import com.kitri.user.dto.UserDto;

/**
 * Users Business Logic 구현
 * @author msj0319
 * */
@Service
public class UserServiceImpl implements UserService {
	
	UserRepository dao;
	
	@Autowired
	public UserServiceImpl(UserRepository dao) {
		this.dao = dao;
	}

	@Override
	public UserDto regist(UserDto user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		// email, password, name
		
		// userId 추가, password 암호화
		user.setUserId(UUID.randomUUID().toString());
		user.setEncryptedPassword(
				BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())
				);
		// dao 등록 호출
		UserEntity userEntity = mapper.map(user, UserEntity.class);
		dao.save(userEntity);
		
		return user;
	}

	@Override
	public UserDto getUser(String userId) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = dao.findByUserId(userId);
		
		return mapper.map(userEntity, UserDto.class);
	}

	@Override
	public List<UserDto> getUsers() {
		List<UserDto> users = new ArrayList<UserDto>();
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Iterable<UserEntity> list = dao.findAll();
		
		list.forEach(userEntity -> {
			users.add(mapper.map(userEntity, UserDto.class));
		});
		
		return users;
	}

	@Override
	public UserDto modifyUser(UserDto user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		//변경할 비밀번호는 암호화 과정을 거친 후에 리턴
		user.setEncryptedPassword(
				BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())
				);
		
		//수정할 유저 정보를 가져와서 업데이트 할 데이터 set
		UserEntity userEntity = dao.findByUserId(user.getUserId());
		if (userEntity != null) {
			userEntity.setName(user.getName());
			userEntity.setEmail(user.getEmail());
			userEntity.setEncryptedPassword(user.getEncryptedPassword());
		}
		dao.save(userEntity);
		
		return user;
	}

	@Override
	public String removeUser(String userId) {
		dao.deleteById(dao.findByUserId(userId).getId());
		return userId;
	}
	
}
