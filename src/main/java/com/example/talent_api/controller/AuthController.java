package com.example.talent_api.controller;

import com.example.talent_api.dto.LoginRequest;
import com.example.talent_api.dto.RegistrationRequest;
import com.example.talent_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
		return authService.register(registrationRequest);
	}
}
