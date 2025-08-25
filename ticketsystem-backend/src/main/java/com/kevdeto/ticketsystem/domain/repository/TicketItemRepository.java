package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;

@Repository
public interface TicketItemRepository extends JpaRepository<TicketItemEntity, Long> {
    // Validar que el item pertenece a un ticket de cierto negocio
//    @Query("SELECT CASE WHEN COUNT(ti) > 0 THEN TRUE ELSE FALSE END " +
//           "FROM TicketItemEntity ti " +
//           "WHERE ti.id = :itemId AND ti.ticket.business.id = :businessId")
//    boolean existsByIdAndBusinessId(@Param("itemId") Long itemId, @Param("businessId") Long businessId);
}
