package com.kevdeto.ticketsystem.domain.dto.response;

public record TicketItemResponseDTO(
	    Long id,
	    Integer quantity,
	    Double subtotal,
	    Long productId
	) {}
