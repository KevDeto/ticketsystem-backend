package com.kevdeto.ticketsystem.auth.application.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.auth.domain.dto.AuthRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.AuthResponseDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RefreshRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.dto.RegisterRequestDTO;
import com.kevdeto.ticketsystem.auth.domain.enums.UserRole;
import com.kevdeto.ticketsystem.auth.domain.model.RefreshTokenEntity;
import com.kevdeto.ticketsystem.auth.domain.model.UserEntity;
import com.kevdeto.ticketsystem.auth.domain.repository.RefreshTokenRepository;
import com.kevdeto.ticketsystem.auth.domain.repository.UserRepository;
import com.kevdeto.ticketsystem.auth.security.jwt.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public AuthResponseDTO login(AuthRequestDTO request, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String email = request.getEmail();
			String accessToken = jwtUtils.generateAccessToken(email);
			createAndPersistRefreshToken(email, response);
			
			return new AuthResponseDTO("Login successful", accessToken, null);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username or password", e);
		}
	}

	public AuthResponseDTO register(RegisterRequestDTO request, HttpServletResponse response) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email already exists");
		}

		UserEntity newUser = new UserEntity();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setEmail(request.getEmail());
		newUser.setRole(UserRole.USER);

		userRepository.save(newUser);

		String accessToken  = jwtUtils.generateAccessToken(newUser.getEmail());
		createAndPersistRefreshToken(newUser.getEmail(), response);

		return new AuthResponseDTO("User registered successfully", accessToken , null);
	}

	public AuthResponseDTO refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    String oldRefreshToken = Arrays.stream(cookies)
	            .filter(c -> "refreshToken".equals(c.getName()))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElseThrow(() -> new RuntimeException("No refresh token cookie"));
		
		if (!jwtUtils.isTokenValid(oldRefreshToken, "refresh")) {
			throw new RuntimeException("Refresh token is invalid, has expired, or is not of the 'refresh' type.");
		}
		String email = jwtUtils.extractEmail(oldRefreshToken);
		String oldJti = jwtUtils.extractJti(oldRefreshToken);

		RefreshTokenEntity tokenEntity = refreshTokenRepository.findByJti(oldJti)
				.orElseThrow(() -> new RuntimeException("Refresh token no encontrado en DB"));

		if (tokenEntity.isRevoked() || tokenEntity.isExpired()) {
			// 🚨 posible reutilización → revocar todo del usuario
			refreshTokenRepository.deleteByUserEmail(email);
			throw new RuntimeException("Refresh token expirado o revocado");
		}
		
		// Generar nuevo access token
		String newAccessToken = jwtUtils.generateAccessToken(email);
		rotateRefreshToken(tokenEntity, email, response);
		
		return new AuthResponseDTO("Token actualizado correctamente", newAccessToken, null);
	}
	
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge((int) (jwtUtils.getRefreshTokenExpirationMillis()) / 1000)
                .sameSite("Strict")//para dominios distintos utilizar: sameSite("None").secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    
    private void rotateRefreshToken(
            RefreshTokenEntity oldEntity, String email, HttpServletResponse response) {

        // Revocar el anterior
        oldEntity.setRevokedAt(Instant.now());

        // Crear nuevo
        String newJti = UUID.randomUUID().toString();
        String newRefreshToken = jwtUtils.generateRefreshToken(email, newJti);

        RefreshTokenEntity newEntity = new RefreshTokenEntity();
        newEntity.setJti(newJti);
        newEntity.setUserEmail(email);
        newEntity.setIssuedAt(Instant.now());
        newEntity.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpirationMillis()));
        refreshTokenRepository.save(newEntity);

        // Vincular anterior → nuevo
        oldEntity.setReplacedByJti(newJti);
        refreshTokenRepository.save(oldEntity);

        setRefreshTokenCookie(response, newRefreshToken);
    }
    
    private void createAndPersistRefreshToken(String email, HttpServletResponse response) {
        String jti = UUID.randomUUID().toString();
        String refreshToken = jwtUtils.generateRefreshToken(email, jti);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setJti(jti);
        entity.setUserEmail(email);
        entity.setIssuedAt(Instant.now());
        entity.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpirationMillis()));
        refreshTokenRepository.save(entity);

        setRefreshTokenCookie(response, refreshToken);
    }
}
