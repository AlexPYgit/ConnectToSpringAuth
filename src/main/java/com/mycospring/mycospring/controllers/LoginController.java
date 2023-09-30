package com.mycospring.mycospring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycospring.model.mycospring.model.dto.BearerTokenDTO;
import com.mycospring.model.mycospring.model.dto.LoginDTO;
import com.mycospring.model.mycospring.model.dto.SignupDTO;
import com.mycospring.mycospring.services.IUserSecurityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	private IUserSecurityService userSecurityService;

	// connect with token or form if not token
	@PostMapping("/signin")
	public BearerTokenDTO authenticateUser(@RequestBody LoginDTO rLoginDTO) {
		return userSecurityService.authenticate(rLoginDTO);
	}

	@PostMapping("/admin")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> createAdmin(@RequestBody SignupDTO rSignup) {
		rSignup.setRole("ADMIN");
		return userSecurityService.register(rSignup);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUer(@RequestBody SignupDTO rSignup) {
		return userSecurityService.register(rSignup);
	}
}
