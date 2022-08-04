package com.product.manager.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UserLoginDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String email;
	
	@NotNull
	private String password;
	
	public UserLoginDTO() {
		super();
	}
	
	public UserLoginDTO(@NotNull String email, @NotNull String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
