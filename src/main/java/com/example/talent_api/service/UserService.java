package com.example.talent_api.service;

import com.example.talent_api.dto.UserDto;
import com.example.talent_api.model.User;
import com.example.talent_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	public UserDto getUserById(Long id) {
		return userRepository.findById(id)
				.map(this::convertToDto)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public UserDto createUser(UserDto userDto) {
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRoles(userDto.getRoles());
		return convertToDto(userRepository.save(user));
	}

	public UserDto updateUser(Long id, UserDto userDto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}
		user.setRoles(userDto.getRoles());
		return convertToDto(userRepository.save(user));
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	private UserDto convertToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setRoles(user.getRoles());
		return userDto;
	}
}