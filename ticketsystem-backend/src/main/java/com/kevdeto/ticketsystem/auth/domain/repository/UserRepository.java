package com.kevdeto.ticketsystem.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.auth.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
