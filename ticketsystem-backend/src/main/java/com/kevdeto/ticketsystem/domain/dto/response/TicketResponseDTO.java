package com.kevdeto.ticketsystem.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record TicketResponseDTO(
	    Long id,
	    LocalDateTime issueDate,
	    Double total,
	    Long businessId,
	    List<TicketItemResponseDTO> items
	) {}