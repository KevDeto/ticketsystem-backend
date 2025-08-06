package com.kevdeto.ticketsystem.domain.dto.request;

public record TicketItemRequestDTO(
	    Integer quantity,
	    Double subtotal,
	    Long productId
	) {}
