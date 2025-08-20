package com.kevdeto.ticketsystem.auth.controller;

import java.util.Arrays;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.auth.application.service.AuthService;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthResponseDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RefreshRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RegisterRequestDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

//	@PreAuthorize("hasRole('USER')")
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request,
			HttpServletResponse response) {
		return ResponseEntity.ok(authService.login(request, response));
	}

//	@PreAuthorize("hasRole('USER')")
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request,
			HttpServletResponse response) {
		return ResponseEntity.ok(authService.register(request, response));
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDTO> refreshAccessToken(HttpServletRequest request,
			HttpServletResponse response) {
		return ResponseEntity.ok(authService.refreshAccessToken(request, response));
	}
}
