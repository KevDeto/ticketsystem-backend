package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;

public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {

}
