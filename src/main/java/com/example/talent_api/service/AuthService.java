package com.example.talent_api.service;

import com.example.talent_api.dto.LoginRequest;
import com.example.talent_api.dto.RegistrationRequest;
import com.example.talent_api.model.User;
import com.example.talent_api.repository.UserRepository;
import com.example.talent_api.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
	                   PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		logger.info("AuthService initialized");
	}

	public ResponseEntity<?> login(LoginRequest loginRequest) {
		logger.info("Login attempt for user: {}", loginRequest.getUsername());
		try {
			logger.debug("Attempting to authenticate user");
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
			);
			logger.info("Authentication successful for user: {}", loginRequest.getUsername());

			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.debug("Set authentication in SecurityContext");

			String jwt = jwtUtil.generateToken(authentication);
			logger.debug("JWT token generated for user: {}", loginRequest.getUsername());

			Map<String, String> response = new HashMap<>();
			response.put("token", jwt);
			logger.info("Login successful for user: {}", loginRequest.getUsername());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
			return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
		}
	}

	public ResponseEntity<?> register(RegistrationRequest registrationRequest) {
		logger.info("Registration attempt for user: {}", registrationRequest.getUsername());

		if (userRepository.existsByUsername(registrationRequest.getUsername())) {
			logger.warn("Registration failed: Username '{}' is already taken", registrationRequest.getUsername());
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(registrationRequest.getEmail())) {
			logger.warn("Registration failed: Email '{}' is already in use", registrationRequest.getEmail());
			return ResponseEntity.badRequest().body("Error: Email is already in use!");
		}

		try {
			User user = new User(
					registrationRequest.getUsername(),
					passwordEncoder.encode(registrationRequest.getPassword()),
					registrationRequest.getEmail()
			);
			logger.debug("Created new user object");

			user.setRoles(Collections.singleton("ROLE_USER"));
			logger.debug("Set default role for user");

			userRepository.save(user);
			logger.info("User registered successfully: {}", registrationRequest.getUsername());

			return ResponseEntity.ok("User registered successfully!");
		} catch (Exception e) {
			logger.error("Error occurred while registering user: {}", registrationRequest.getUsername(), e);
			return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
		}
	}
}
