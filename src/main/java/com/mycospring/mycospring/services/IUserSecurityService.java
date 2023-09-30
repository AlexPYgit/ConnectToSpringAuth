package com.mycospring.mycospring.services;

import org.springframework.http.ResponseEntity;

import com.mycospring.model.mycospring.model.dto.BearerTokenDTO;
import com.mycospring.model.mycospring.model.dto.LoginDTO;
import com.mycospring.model.mycospring.model.dto.SignupDTO;

public interface IUserSecurityService {

	/**
	 * enregistre le nouveau user en base
	 * 
	 * @param rSignUpDto : donnees en entree
	 * @return le BearerToken ou msg d'erreur avec statut en erreur
	 */
	ResponseEntity<?> register(SignupDTO rSignUpDto);

	/**
	 * authentifie le user
	 * 
	 * @param rLoginDto : donnees en entree
	 * @return le BearerToken ou erreur d'authentification
	 */
	BearerTokenDTO authenticate(LoginDTO rLoginDto);
}
