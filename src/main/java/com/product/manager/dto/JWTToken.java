package com.product.manager.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken implements Serializable {

	private static final long serialVersionUID = 1L;
	private String idToken;

    public JWTToken(String idToken) {
        this.idToken = idToken;
    }

    public JWTToken() {
		super();
	}

	@JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
