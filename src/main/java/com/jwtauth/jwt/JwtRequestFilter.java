package com.jwtauth.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtauth.securityconfig.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = null;
		String username = null;
		
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			username = jwtUtils.extractUsername(jwt);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = 
					userDetailsService.loadUserByUsername(username);
			
		if(jwtUtils.validateToken(jwt, userDetails)) {
			UsernamePasswordAuthenticationToken tok = 
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			tok.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(tok);
		}
			
		}
		
		filterChain.doFilter(request, response);

	}

}
