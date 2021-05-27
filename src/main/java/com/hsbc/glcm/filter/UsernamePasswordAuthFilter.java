package com.hsbc.glcm.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.glcm.model.CredentialsDto;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("filter calling");
		
		if ("/v1/signIn".equals(request.getServletPath())
                && HttpMethod.POST.matches(request.getMethod())) {
            CredentialsDto credentialsDto = MAPPER.readValue(request.getInputStream(), CredentialsDto.class);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword()));
        }

        filterChain.doFilter(request, response);
	}

}
