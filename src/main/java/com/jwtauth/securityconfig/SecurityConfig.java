package com.jwtauth.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwtauth.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Autowired
	private JwtRequestFilter filter;
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration configuration) throws Exception	{
		return configuration.getAuthenticationManager();
	}
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(myUserDetailsService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeHttpRequests((req) -> 
		req.requestMatchers("/auth").permitAll()
		.anyRequest().authenticated()
		).sessionManagement((n) -> n.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
}
