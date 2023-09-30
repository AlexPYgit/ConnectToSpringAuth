package com.mycospring.mycospring.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycospring.mycospring.dao.IUserRepository;
import com.mycospring.mycospring.model.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	private final IUserRepository IUserRepository;

	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Recuper le user selon son nom ou son email sachant que chacun est UNIQUE en
		// base
		UserEntity user = IUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu : " + usernameOrEmail));

		// Recupere les roles du user
		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());

		// Renvoie l'objet UserDetails utilise par Spring Security
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}
