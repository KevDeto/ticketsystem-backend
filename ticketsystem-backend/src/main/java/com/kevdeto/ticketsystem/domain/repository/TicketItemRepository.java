package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;

public interface TicketItemRepository extends JpaRepository<TicketItemEntity, Long> {

}
