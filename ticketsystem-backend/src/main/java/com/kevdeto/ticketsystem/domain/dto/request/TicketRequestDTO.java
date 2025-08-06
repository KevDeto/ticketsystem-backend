package com.kevdeto.ticketsystem.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record TicketRequestDTO(
	    LocalDateTime issueDate,
	    Double total,
	    Long businessId,
	    List<TicketItemRequestDTO> items
	) {}
