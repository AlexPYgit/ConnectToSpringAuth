package com.mycospring.mycospring.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//add filter JwtAuthentication before the filterChain of the spring security. for authenticate the user with this name et this role.
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtilities jwtUtilities;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse responce,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		// get token in the request
		String token = jwtUtilities.getToken(request);

		// if token ok
		if (token != null && jwtUtilities.validateToken(token)) {
			// claims username of user in the token
			String username = jwtUtilities.extractUsername(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// authenticate the user
			if (userDetails != null) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, userDetails.getAuthorities());
				log.debug("authenticate user with username : {}", username);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		// continue the chain of traitement
		filterChain.doFilter(request, responce);
	}

}
