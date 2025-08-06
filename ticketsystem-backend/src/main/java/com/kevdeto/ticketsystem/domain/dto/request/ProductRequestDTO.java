package com.kevdeto.ticketsystem.domain.dto.request;

public record ProductRequestDTO(
	    String name,
	    String code,
	    Double price,
	    Long businessId
	) {}
