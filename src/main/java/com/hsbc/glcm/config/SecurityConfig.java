package com.hsbc.glcm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.hsbc.glcm.filter.CookieAuthenticationFilter;
import com.hsbc.glcm.filter.UsernamePasswordAuthFilter;
import com.hsbc.glcm.service.UserAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private  UserAuthenticationEntryPoint userAuthenticationEntryPoint;
	
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;
	
	
	
	
	  @Override 
	  protected void configure(HttpSecurity http) throws Exception{
		  
		  http
				  .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
		          .and()
		          .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
		          .addFilterBefore(new CookieAuthenticationFilter(), BasicAuthenticationFilter.class)
		          .csrf().disable()				  
		          //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		         // .and()
		          .headers().frameOptions().disable()
		          .and().logout().deleteCookies(CookieAuthenticationFilter.COOKIE_NAME)
		          .and()
		          .authorizeRequests()	          
		          .antMatchers("/v1/signIn","/h2/**").permitAll()
		          .anyRequest().authenticated();
	  
	  }

}
