package com.kevdeto.ticketsystem.application.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.application.usecase.TicketUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.request.TicketRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;
import com.kevdeto.ticketsystem.domain.mapper.TicketMapper;
import com.kevdeto.ticketsystem.domain.repository.BusinessRepository;
import com.kevdeto.ticketsystem.domain.repository.ProductRepository;
import com.kevdeto.ticketsystem.domain.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TicketServiceImpl implements TicketUseCase {
	private final TicketRepository ticketRepository;
	private final BusinessRepository businessRepository;
	private final ProductRepository productRepository;

	private final TicketMapper ticketMapper;

	public TicketServiceImpl(TicketRepository ticketRepository, BusinessRepository businessRepository,
			ProductRepository productRepository, TicketMapper ticketMapper) {
		this.ticketRepository = ticketRepository;
		this.businessRepository = businessRepository;
		this.productRepository = productRepository;
		this.ticketMapper = ticketMapper;
	}

	@Override
	@Transactional
	public TicketResponseDTO create(TicketRequestDTO dto) {
		BusinessEntity business = businessRepository.findById(dto.businessId())
				.orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado"));

		TicketEntity ticket = new TicketEntity();
		ticket.setIssueDate(dto.issueDate());
		ticket.setBusiness(business);

		List<TicketItemEntity> items = buildItemsAndCalculateTotal(ticket, dto.items());
		BigDecimal total = items.stream().map(TicketItemEntity::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		ticket.setItems(items);
		ticket.setTotal(total);

		TicketEntity saved = ticketRepository.save(ticket);
		return ticketMapper.toResponse(saved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (!ticketRepository.existsById(id)) {
			throw new EntityNotFoundException("Ticket no encontrado");
		}
		ticketRepository.deleteById(id);
	}

	@Override
	public TicketResponseDTO getById(Long id) {
		TicketEntity entity = ticketRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado"));
		return ticketMapper.toResponse(entity);
	}

	@Override
	public List<TicketResponseDTO> getAll() {
		return ticketRepository.findAll().stream().map(ticketMapper::toResponse).toList();
	}

	private List<TicketItemEntity> buildItemsAndCalculateTotal(TicketEntity ticket,
			List<TicketItemRequestDTO> itemDTOs) {
		List<TicketItemEntity> items = new ArrayList<>();

		for (TicketItemRequestDTO itemDTO : itemDTOs) {
			ProductEntity product = productRepository.findById(itemDTO.productId()).orElseThrow(
					() -> new EntityNotFoundException("Producto con ID " + itemDTO.productId() + " no encontrado"));

			TicketItemEntity item = new TicketItemEntity();
			item.setProduct(product);
			item.setQuantity(itemDTO.quantity());

			BigDecimal price = new BigDecimal(product.getPrice());
			BigDecimal subtotal = price.multiply(new BigDecimal(itemDTO.quantity()));
			item.setSubtotal(subtotal);
			item.setTicket(ticket); // relacion inversa

			items.add(item);
		}

		return items;
	}
}
