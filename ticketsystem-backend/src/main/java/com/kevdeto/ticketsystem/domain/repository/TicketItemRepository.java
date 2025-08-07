package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;

@Repository
public interface TicketItemRepository extends JpaRepository<TicketItemEntity, Long> {

}
