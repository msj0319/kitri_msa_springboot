package com.kitri.user.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Domain - JPA
 * @author msj0319
 * */
@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "user_id", nullable = false, unique = true)
	private String userId;
	@Column(length = 30)
	private String name;
	@Column(nullable = false, length = 50, unique = true)
	private String email;
	@Column(name="encrypted_password", nullable = false, unique = true)
	private String encryptedPassword;
	
	//UserRepository 테스트를 위한 생성자
	@Builder
	public UserEntity(String userId, String email, String encryptedPassword) {
		super();
		this.email = email;
		this.userId = userId;
		this.userId = encryptedPassword;
	}
}
