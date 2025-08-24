package com.kevdeto.ticketsystem.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kevdeto.ticketsystem.application.usecase.UserUseCase;
import com.kevdeto.ticketsystem.auth.domain.enums.UserRole;
import com.kevdeto.ticketsystem.auth.domain.model.UserEntity;
import com.kevdeto.ticketsystem.auth.domain.repository.UserRepository;
import com.kevdeto.ticketsystem.domain.dto.request.UserRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.UserResponseDTO;
import com.kevdeto.ticketsystem.domain.mapper.UserMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserUseCase {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	@Transactional
	public UserResponseDTO create(UserRequestDTO dto) {
		UserEntity entity = userMapper.toEntity(dto);
		entity.setRole(UserRole.USER);//provisorio?
		// nota: en el futuro tengo que hashear el password en este metodo o por aca
		UserEntity saved = userRepository.save(entity);
		return userMapper.toResponse(saved);
	}

	@Override
	@Transactional
	public UserResponseDTO update(Long id, UserRequestDTO dto) {
		UserEntity entity = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

		userMapper.updateEntityFromRequest(dto, entity);
		UserEntity updated = userRepository.save(entity);
		return userMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new EntityNotFoundException("Usuario no encontrado");
		}
		userRepository.deleteById(id);
	}

	@Override
	public UserResponseDTO getById(Long id) {
		UserEntity entity = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
		return userMapper.toResponse(entity);
	}

	@Override
	public List<UserResponseDTO> getAll() {
		return userRepository.findAll().stream().map(userMapper::toResponse).toList();
	}
}
