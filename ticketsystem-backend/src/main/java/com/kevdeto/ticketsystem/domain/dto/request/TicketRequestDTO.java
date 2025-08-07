package com.kevdeto.ticketsystem.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record TicketRequestDTO(
	    LocalDateTime issueDate,
	    Long businessId,
	    List<TicketItemRequestDTO> items
	) {}
