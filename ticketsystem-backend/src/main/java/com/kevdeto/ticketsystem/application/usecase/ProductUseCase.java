package com.kevdeto.ticketsystem.application.usecase;

import java.util.List;

import com.kevdeto.ticketsystem.domain.dto.request.ProductRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.PaginatedResponseDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;

public interface ProductUseCase {
	ProductResponseDTO create(ProductRequestDTO dto);

	ProductResponseDTO update(Long id, ProductRequestDTO dto);

	void delete(Long id);

	ProductResponseDTO getById(Long id);

	List<ProductResponseDTO> getAll();

	PaginatedResponseDTO<ProductResponseDTO> getByBusinessId(Long id, int page, int size);
}
