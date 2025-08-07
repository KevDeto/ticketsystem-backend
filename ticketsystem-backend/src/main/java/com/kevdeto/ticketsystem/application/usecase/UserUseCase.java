package com.kevdeto.ticketsystem.application.usecase;

import java.util.List;

import com.kevdeto.ticketsystem.domain.dto.request.UserRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.UserResponseDTO;

public interface UserUseCase {
	UserResponseDTO create(UserRequestDTO dto);

	UserResponseDTO update(Long id, UserRequestDTO dto);

	void delete(Long id);

	UserResponseDTO getById(Long id);

	List<UserResponseDTO> getAll();
}
