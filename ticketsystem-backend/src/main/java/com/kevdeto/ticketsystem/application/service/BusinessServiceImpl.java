package com.kevdeto.ticketsystem.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.application.usecase.BusinessUseCase;
import com.kevdeto.ticketsystem.auth.domain.UserEntity;
import com.kevdeto.ticketsystem.auth.domain.repository.UserRepository;
import com.kevdeto.ticketsystem.domain.dto.request.BusinessRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.BusinessResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.mapper.BusinessMapper;
import com.kevdeto.ticketsystem.domain.repository.BusinessRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BusinessServiceImpl implements BusinessUseCase {
	private final BusinessRepository businessRepository;
	private final BusinessMapper businessMapper;
	private final UserRepository userRepository;

	public BusinessServiceImpl(BusinessRepository businessRepository, BusinessMapper businessMapper,
			UserRepository userRepository) {
		this.businessRepository = businessRepository;
		this.businessMapper = businessMapper;
		this.userRepository = userRepository;
	}

	@Override
	public BusinessResponseDTO create(BusinessRequestDTO dto) {
		UserEntity user = userRepository.findById(dto.userId())
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

		BusinessEntity entity = businessMapper.toEntity(dto);
		entity.setUser(user);

		BusinessEntity saved = businessRepository.save(entity);
		return businessMapper.toResponse(saved);
	}

	@Override
	public BusinessResponseDTO update(Long id, BusinessRequestDTO dto) {
		BusinessEntity entity = businessRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado"));

		if (!userRepository.existsById(dto.userId())) {
			throw new EntityNotFoundException("Usuario no encontrado");
		}

		businessMapper.updateEntityFromRequest(dto, entity);
		BusinessEntity updated = businessRepository.save(entity);
		return businessMapper.toResponse(updated);
	}

	@Override
	public void delete(Long id) {
		if (!businessRepository.existsById(id)) {
			throw new EntityNotFoundException("Negocio no encontrado");
		}
		businessRepository.deleteById(id);
	}

	@Override
	public BusinessResponseDTO getById(Long id) {
		BusinessEntity entity = businessRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado"));
		return businessMapper.toResponse(entity);
	}

	@Override
	public List<BusinessResponseDTO> getAll() {
		return businessRepository.findAll().stream().map(businessMapper::toResponse).toList();
	}
}
