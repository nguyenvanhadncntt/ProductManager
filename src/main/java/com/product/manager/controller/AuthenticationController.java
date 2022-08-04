package com.product.manager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.product.manager.dto.JWTToken;
import com.product.manager.dto.UserLoginDTO;
import com.product.manager.jwt.JWTRequestFilter;
import com.product.manager.jwt.TokenProvider;

@RestController
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@PostMapping("/authenticate")
	public ResponseEntity<JWTToken> authenticate(@Valid @RequestBody UserLoginDTO userLogin) {
		UsernamePasswordAuthenticationToken authenticationToken =
	            new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

	        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = tokenProvider.createToken(authentication);
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.add(JWTRequestFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
	}
}
