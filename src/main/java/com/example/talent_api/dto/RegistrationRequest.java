package com.example.talent_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {
	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	@NotBlank
	@Size(max = 100)
	@Email
	private String email;

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}