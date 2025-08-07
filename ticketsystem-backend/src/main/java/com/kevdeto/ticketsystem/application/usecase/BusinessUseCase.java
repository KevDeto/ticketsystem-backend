package com.kevdeto.ticketsystem.application.usecase;

import java.util.List;

import com.kevdeto.ticketsystem.domain.dto.request.BusinessRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.BusinessResponseDTO;

public interface BusinessUseCase {
	BusinessResponseDTO create(BusinessRequestDTO dto);

	BusinessResponseDTO update(Long id, BusinessRequestDTO dto);

	void delete(Long id);

	BusinessResponseDTO getById(Long id);

	List<BusinessResponseDTO> getAll();
}
