package com.kevdeto.ticketsystem.application.usecase;

import java.util.List;

import com.kevdeto.ticketsystem.domain.dto.request.TicketRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketResponseDTO;

public interface TicketUseCase {
	TicketResponseDTO create(TicketRequestDTO dto);

	void delete(Long id);

	TicketResponseDTO getById(Long id);

	List<TicketResponseDTO> getAll();
}
