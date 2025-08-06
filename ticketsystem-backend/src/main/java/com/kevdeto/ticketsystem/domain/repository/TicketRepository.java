package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevdeto.ticketsystem.domain.entity.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

}
