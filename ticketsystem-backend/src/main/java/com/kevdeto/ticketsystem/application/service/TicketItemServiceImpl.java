package com.kevdeto.ticketsystem.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.application.usecase.TicketItemUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketItemResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;
import com.kevdeto.ticketsystem.domain.mapper.TicketItemMapper;
import com.kevdeto.ticketsystem.domain.repository.ProductRepository;
import com.kevdeto.ticketsystem.domain.repository.TicketItemRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TicketItemServiceImpl implements TicketItemUseCase {
	private final TicketItemRepository ticketItemRepository;
	private final TicketItemMapper ticketItemMapper;
	private final ProductRepository productRepository;

	public TicketItemServiceImpl(TicketItemRepository ticketItemRepository, TicketItemMapper ticketItemMapper,
			ProductRepository productRepository) {
		this.ticketItemRepository = ticketItemRepository;
		this.ticketItemMapper = ticketItemMapper;
		this.productRepository = productRepository;
	}

	@Override
	@Transactional
	public TicketItemResponseDTO update(Long id, TicketItemRequestDTO dto) {
		TicketItemEntity entity = ticketItemRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ítem de ticket no encontrado"));

		if (!productRepository.existsById(dto.productId())) {
			throw new EntityNotFoundException("Producto no encontrado");
		}

		ticketItemMapper.updateEntityFromRequest(dto, entity);
		TicketItemEntity updated = ticketItemRepository.save(entity);
		return ticketItemMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (!ticketItemRepository.existsById(id)) {
			throw new EntityNotFoundException("Ítem de ticket no encontrado");
		}
		ticketItemRepository.deleteById(id);
	}

	@Override
	public TicketItemResponseDTO getById(Long id) {
		TicketItemEntity entity = ticketItemRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ítem de ticket no encontrado"));
		return ticketItemMapper.toResponse(entity);
	}

	@Override
	public List<TicketItemResponseDTO> getAll() {
		return ticketItemRepository.findAll().stream().map(ticketItemMapper::toResponse).toList();
	}
}
