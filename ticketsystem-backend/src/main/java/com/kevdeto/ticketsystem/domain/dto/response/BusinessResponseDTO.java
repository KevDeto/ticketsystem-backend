package com.kevdeto.ticketsystem.domain.dto.response;

public record BusinessResponseDTO(
	    Long id,
	    String name,
	    String adress,
	    String telephone,
	    String ownerName,
	    Long userId
	) {}
