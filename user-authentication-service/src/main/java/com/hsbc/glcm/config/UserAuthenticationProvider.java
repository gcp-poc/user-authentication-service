package com.hsbc.glcm.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.hsbc.glcm.model.CredentialsDto;
import com.hsbc.glcm.model.UserDto;
import com.hsbc.glcm.service.AuthenticationService;



@Component
public class UserAuthenticationProvider implements AuthenticationProvider{
	
	 @Autowired
	 private  AuthenticationService authenticationService;

	 
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		 UserDto userDto = null;
		 CredentialsDto credentialsDto = new CredentialsDto((String)authentication.getPrincipal(),(String) authentication.getCredentials());
		 
	        if (authentication instanceof UsernamePasswordAuthenticationToken) {
	        	
	        	System.out.println("AUth by user & pwd::"+(String) authentication.getPrincipal());

	            // authentication by username and password
	            userDto = authenticationService.authenticate(credentialsDto);
	        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
	            // authentication by cookie
	        	System.out.println("AUth by token::"+(String) authentication.getPrincipal());
	             userDto = authenticationService.findByToken((String) authentication.getPrincipal());
	        }

	        if (userDto == null) {
	            return null;
	        }

	        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
