package com.hsbc.glcm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.glcm.model.UserDto;

@Repository
public interface AuthenticationRepository extends JpaRepository<UserDto,Long>{

	UserDto findByUsername(String username);
	
}
