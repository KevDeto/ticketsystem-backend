package com.kevdeto.ticketsystem.domain.dto.request;

public record BusinessRequestDTO(
	    String name,
	    String adress,
	    String telephone,
	    String ownerName,
	    Long userId
	) {}
