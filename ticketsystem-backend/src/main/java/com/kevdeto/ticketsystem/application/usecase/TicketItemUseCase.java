package com.kevdeto.ticketsystem.application.usecase;

import java.util.List;

import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketItemResponseDTO;

public interface TicketItemUseCase {
	TicketItemResponseDTO update(Long id, TicketItemRequestDTO dto);

	void delete(Long id);

	TicketItemResponseDTO getById(Long id);

	List<TicketItemResponseDTO> getAll();
}
