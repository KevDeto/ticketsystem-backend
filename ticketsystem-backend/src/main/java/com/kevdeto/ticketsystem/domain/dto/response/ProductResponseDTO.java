package com.kevdeto.ticketsystem.domain.dto.response;

public record ProductResponseDTO(
	    Long id,
	    String name,
	    String code,
	    Double price,
	    Long businessId
	) {}
