package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kevdeto.ticketsystem.domain.dto.request.BusinessRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.BusinessResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
	@Mapping(source = "user.id", target = "userId")
	BusinessResponseDTO toResponse(BusinessEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "products", ignore = true)
	@Mapping(target = "user", ignore = true)
	BusinessEntity toEntity(BusinessRequestDTO dto);
}
