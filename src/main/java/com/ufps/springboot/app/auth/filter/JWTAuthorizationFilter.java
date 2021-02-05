package com.ufps.springboot.app.auth.filter;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ufps.springboot.app.auth.service.JWTService;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	//public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	private JWTService jwtService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

		String header = request.getHeader("authorization");

		if (!requiresAuthentication(header)) {
			chain.doFilter(request, response);
			return;

		}

		
		UsernamePasswordAuthenticationToken authentication = null;
		
		if (jwtService.validate(header)) {
						
			
			authentication = new UsernamePasswordAuthenticationToken(jwtService.GetUsername(header),null, jwtService.getRoles(header));
			
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	

	protected boolean requiresAuthentication(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return false;

		}
		return true;

	}

}
