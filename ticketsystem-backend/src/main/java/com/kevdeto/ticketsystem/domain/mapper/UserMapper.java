package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kevdeto.ticketsystem.auth.domain.model.UserEntity;
import com.kevdeto.ticketsystem.domain.dto.request.UserRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", ignore = true)
	@Mapping(target = "role", ignore = true)
	UserEntity toEntity(UserRequestDTO dto);

	UserResponseDTO toResponse(UserEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", ignore = true)
	@Mapping(target = "role", ignore = true)
	void updateEntityFromRequest(UserRequestDTO dto, @MappingTarget UserEntity entity);
}