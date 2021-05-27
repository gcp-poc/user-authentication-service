package com.hsbc.glcm.controller;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.glcm.filter.CookieAuthenticationFilter;
import com.hsbc.glcm.model.UserDto;
import com.hsbc.glcm.service.AuthenticationService;

@RestController
@RequestMapping("/v1")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	@GetMapping("/getusers")
	public UserDto getUserDetails() {
		return authenticationService.findByUsername("admin1");
	}
	
	
	@PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user,
                                          HttpServletResponse servletResponse) {

		
		  Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, authenticationService.createToken(user)); 
		  authCookie.setHttpOnly(true);
		  authCookie.setSecure(true);
		  authCookie.setMaxAge((int) Duration.of(1,ChronoUnit.DAYS).toSeconds());
		  authCookie.setPath("/");		  
		  
		  servletResponse.addCookie(authCookie);		 

        return ResponseEntity.ok(user);
    }
	
	@PostMapping("/signOut")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

}
