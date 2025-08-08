package com.kevdeto.ticketsystem.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TicketResponseDTO(
	    Long id,
	    LocalDateTime issueDate,
	    BigDecimal total,
	    Long businessId,
	    List<TicketItemResponseDTO> items
	) {}