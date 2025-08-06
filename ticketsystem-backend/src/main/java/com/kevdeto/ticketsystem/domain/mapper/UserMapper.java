package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kevdeto.ticketsystem.auth.domain.UserEntity;
import com.kevdeto.ticketsystem.domain.dto.request.UserRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponse(UserEntity entity);
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserRequestDTO dto);
}