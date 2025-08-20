package com.kevdeto.ticketsystem.auth.domain.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String jti; // UUID del refresh token

	@Column(nullable = false)
	private String userEmail;

	@Column(nullable = false)
	private Instant issuedAt;

	@Column(nullable = false)
	private Instant expiresAt;

	private Instant revokedAt;

	private String replacedByJti; // para rotar

	public boolean isRevoked() {
		return revokedAt != null;
	}

	public boolean isExpired() {
		return Instant.now().isAfter(expiresAt);
	}
}
