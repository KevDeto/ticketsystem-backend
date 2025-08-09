package com.kevdeto.ticketsystem.auth.application.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.auth.domain.dto.AuthRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthResponseDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RegisterRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.enums.UserRole;
import com.kevdeto.ticketsystem.auth.domain.model.UserEntity;
import com.kevdeto.ticketsystem.auth.domain.repository.UserRepository;
import com.kevdeto.ticketsystem.auth.security.jwt.JwtUtils;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
			UserDetailsServiceImpl userDetailsService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	public AuthResponseDTO login(AuthRequestDTO request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String accesToken = jwtUtils.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());
        return new AuthResponseDTO("Login successful", accesToken, refreshToken);
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRole(UserRole.ROLE_USER);

        userRepository.save(newUser);

        String token = jwtUtils.generateAccessToken(newUser.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(newUser.getUsername());

        return new AuthResponseDTO("User registered successfully", token, refreshToken);
    }
    
    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        if (!jwtUtils.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        String username = jwtUtils.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccesToken = jwtUtils.generateAccessToken(userDetails.getUsername());
        return new AuthResponseDTO(username, newAccesToken, refreshToken);
    }
}
