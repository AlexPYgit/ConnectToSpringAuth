package com.mycospring.mycospring.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycospring.model.mycospring.model.dto.BearerTokenDTO;
import com.mycospring.model.mycospring.model.dto.LoginDTO;
import com.mycospring.model.mycospring.model.dto.SignupDTO;
import com.mycospring.mycospring.dao.IRoleRepository;
import com.mycospring.mycospring.dao.IUserRepository;
import com.mycospring.mycospring.model.RoleEntity;
import com.mycospring.mycospring.model.RoleName;
import com.mycospring.mycospring.model.UserEntity;
import com.mycospring.mycospring.security.JwtUtilities;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements IUserSecurityService {
	private final AuthenticationManager authenticationManager;
	private final IUserRepository iUserRepository;

	private final IRoleRepository iRoleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtilities jwtUtilities;

	private static final String TOKEN_TYPE = "Bearer";

	@Override
	public ResponseEntity<?> register(SignupDTO rSignUpDTO) {
		// verifie si username dans la base
		if (iUserRepository.existsByUsername(rSignUpDTO.getUsername())) {
			return new ResponseEntity<>("Username deja pris !", HttpStatus.BAD_REQUEST);
		}

		// verifie si email existe deja dans la base
		if (iUserRepository.existsByEmail(rSignUpDTO.getEmail())) {
			return new ResponseEntity<>("Email deja pris !", HttpStatus.BAD_REQUEST);
		}

		// creation du user en base
		UserEntity user = new UserEntity();
		user.setUsername(rSignUpDTO.getUsername());
		user.setEmail(rSignUpDTO.getEmail());
		user.setPassword(passwordEncoder.encode(rSignUpDTO.getPassword()));

		RoleEntity roles = new RoleEntity();
		// add Admin role
		if (rSignUpDTO.getRole() == "ADMIN") {
			roles = iRoleRepository.findByRole(RoleName.ADMIN).get();
			user.setRoles(Collections.singleton(roles));
		} else {
			// role User pour le user;
			roles.setRole(RoleName.USER);
			user.setRoles(Collections.singleton(roles));
		}

		// Sauvegarde le user
		iUserRepository.save(user);

		// creation du token
		String token = jwtUtilities.generateToken(rSignUpDTO.getEmail(), Collections.singletonList(roles.getRole()));
		// renvoi du token dans la reponse
		return new ResponseEntity<>(new BearerTokenDTO(token, TOKEN_TYPE), HttpStatus.OK);
	}

	@Override
	public BearerTokenDTO authenticate(LoginDTO rLoginDto) {
		// Auth user
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				rLoginDto.getUsernameOrEmail(), rLoginDto.getPassword());
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// recupere user pour generer son token
		UserEntity user = iUserRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		// roles du user
		List<String> rolesNames = new ArrayList<>();
		user.getRoles().forEach(r -> rolesNames.add(r.getRole()));
		return new BearerTokenDTO(jwtUtilities.generateToken(user.getUsername(), rolesNames), TOKEN_TYPE);
	}
}
