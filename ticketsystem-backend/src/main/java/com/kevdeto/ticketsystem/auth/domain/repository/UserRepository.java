package com.kevdeto.ticketsystem.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevdeto.ticketsystem.auth.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
