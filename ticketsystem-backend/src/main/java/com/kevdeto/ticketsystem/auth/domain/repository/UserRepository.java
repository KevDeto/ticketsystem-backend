package com.kevdeto.ticketsystem.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.auth.domain.enums.UserRole;
import com.kevdeto.ticketsystem.auth.domain.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByRole(UserRole role);

//    boolean existsByIdAndBusinessId(Long userId, Long businessId);
}
