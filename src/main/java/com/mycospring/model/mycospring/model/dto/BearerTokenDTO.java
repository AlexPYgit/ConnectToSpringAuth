package com.mycospring.model.mycospring.model.dto;

import lombok.Data;

@Data
public class BearerTokenDTO {

	private String accesToken;
	private String tokenType;

	public BearerTokenDTO(String accesToken, String tokenType) {
		this.tokenType = tokenType;
		this.accesToken = accesToken;
	}
}
