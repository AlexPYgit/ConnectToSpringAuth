package com.mycospring.model.mycospring.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

	private String usernameOrEmail;
	private String password;
}
