package com.kevdeto.ticketsystem.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.application.usecase.ProductUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.ProductRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.PaginatedResponseDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;
import com.kevdeto.ticketsystem.domain.mapper.ProductMapper;
import com.kevdeto.ticketsystem.domain.repository.BusinessRepository;
import com.kevdeto.ticketsystem.domain.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductUseCase {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final BusinessRepository businessRepository;

	public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
			BusinessRepository businessRepository) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
		this.businessRepository = businessRepository;
	}

	@Override
	@Transactional
	public ProductResponseDTO create(ProductRequestDTO dto) {
		BusinessEntity business = businessRepository.findById(dto.businessId())
				.orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado"));

		ProductEntity entity = productMapper.toEntity(dto);
		entity.setBusiness(business);

		ProductEntity saved = productRepository.save(entity);
		return productMapper.toResponse(saved);
	}

	@Override
	@Transactional
	public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
		ProductEntity entity = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

		if (!businessRepository.existsById(dto.businessId())) {
			throw new EntityNotFoundException("Negocio no encontrado");
		}

		productMapper.updateEntityFromRequest(dto, entity);
		ProductEntity updated = productRepository.save(entity);
		return productMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
			throw new EntityNotFoundException("Producto no encontrado");
		}
		productRepository.deleteById(id);
	}

	@Override
	public ProductResponseDTO getById(Long id) {
		ProductEntity entity = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
		return productMapper.toResponse(entity);
	}

	@Override
	public List<ProductResponseDTO> getAll() {
		return productRepository.findAll().stream().map(productMapper::toResponse).toList();
	}

	@Override
	public PaginatedResponseDTO<ProductResponseDTO> getByBusinessId(Long id, int page, int size) {
		Page<ProductResponseDTO> productsPage = productRepository.findByBusinessId(id, PageRequest.of(page, size))
				.map(productMapper::toResponse);
		List<ProductResponseDTO> products = productsPage.getContent().stream().toList();
		return new PaginatedResponseDTO<>(products, productsPage.getNumber(), productsPage.getSize(),
				productsPage.getTotalPages(), productsPage.getTotalElements());
	}
}
