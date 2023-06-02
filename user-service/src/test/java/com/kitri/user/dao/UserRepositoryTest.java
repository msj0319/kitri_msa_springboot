package com.kitri.user.dao;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Slf4j
class UserRepositoryTest {
	
	@Autowired
	UserRepository dao;	//jpa 테스트 시 리포지토리 빈 등록
	
	@Test
	void regist() {
		//before
		UserEntity user = UserEntity.builder()
					.email("msj0319@gmail.com")
					.userId("userId")
					.encryptedPassword("1234")
					.build();
		//when
		UserEntity save = dao.save(user);
		//then
		Assertions.assertNotNull(save);
	}

}
