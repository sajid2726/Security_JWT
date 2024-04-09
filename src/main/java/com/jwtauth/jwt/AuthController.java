package com.jwtauth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.securityconfig.MyUserDetailsService;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationManager auth;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@PostMapping("/auth")
	public String authUser(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			auth.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (Exception e) {
			throw new Exception("Invalid Credentials");
		}
		UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
		String token = jwtUtils.generateToken(userDetails);
		System.out.println(token);
		return token;
	}

}
