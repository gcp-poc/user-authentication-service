package com.hsbc.glcm.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hsbc.glcm.model.CredentialsDto;
import com.hsbc.glcm.model.UserDto;
import com.hsbc.glcm.service.repository.AuthenticationRepository;

@Service
public class AuthenticationService {
	
	 @Value("${security.jwt.token.secret-key:secret-key}")
	 private String secretKey;

	
	
	@Autowired
	private AuthenticationRepository authenticationRepository;
	
	public UserDto findByUsername(String username) {
		 return authenticationRepository.findByUsername(username);
	}
	
	
	public UserDto authenticate(CredentialsDto credentialsDto) {
		UserDto  dbUserDto = findByUsername(credentialsDto.getUsername());
		
		if(dbUserDto.getPassword().equals(credentialsDto.getPassword())) {
			return dbUserDto;
		}
		
		return null;
		
	}
	
	 public String createToken(UserDto user) {
	        return user.getId() + "&" + user.getUsername() + "&" + calculateHmac(user);
	 }
	 
	 private String calculateHmac(UserDto user) {
	        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
	        byte[] valueBytes = Objects.requireNonNull(user.getId() + "&" + user.getUsername()).getBytes(StandardCharsets.UTF_8);

	        try {
	            Mac mac = Mac.getInstance("HmacSHA512");
	            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
	            mac.init(secretKeySpec);
	            byte[] hmacBytes = mac.doFinal(valueBytes);
	            return Base64.getEncoder().encodeToString(hmacBytes);

	        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
	            throw new RuntimeException(e);
	        }
	}
	 
	 public UserDto findByToken(String token) {
		 
		 System.out.println("token recieved "+ token);
	        String[] parts = token.split("&");

	        Long userId = Long.valueOf(parts[0]);
	        String username = parts[1];
	        String hmac = parts[2];

	        UserDto userDto = findByUsername(username);

	        if (!hmac.equals(calculateHmac(userDto)) || userId != userDto.getId()) {
	            throw new RuntimeException("Invalid Cookie value");
	        }

	        return userDto;
	 } 

	 
	 

	
}
