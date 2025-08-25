package com.kevdeto.ticketsystem.domain.repository;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.domain.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
//    // ¿El ticket pertenece al negocio X?
//    boolean existsByIdAndBusinessId(Long ticketId, Long businessId);
//
//    // Listar tickets de un negocio
//    Page<TicketEntity> findByBusinessId(Long businessId, Pageable pageable);
}
