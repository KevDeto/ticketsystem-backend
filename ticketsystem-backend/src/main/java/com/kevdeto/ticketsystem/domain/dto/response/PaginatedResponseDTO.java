package com.kevdeto.ticketsystem.domain.dto.response;

import java.util.List;

public record PaginatedResponseDTO<T>(
	    List<T> content,
	    int page,
	    int size,
	    int totalPages,
	    long totalElements
){}
