package com.kevdeto.ticketsystem.domain.dto.request;

public record UserRequestDTO(
	    String username,
	    String email,
	    String password
	) {}
