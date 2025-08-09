package com.kevdeto.ticketsystem.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.auth.application.service.AuthService;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthResponseDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RegisterRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDTO> refreshAccessToken(@RequestBody String refreshToken) {
		return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));
	}
}
