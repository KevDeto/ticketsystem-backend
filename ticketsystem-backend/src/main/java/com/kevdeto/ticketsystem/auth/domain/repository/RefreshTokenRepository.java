package com.kevdeto.ticketsystem.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevdeto.ticketsystem.auth.domain.model.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByJti(String jti);

	void deleteByUserEmail(String email); // para logout global
}
