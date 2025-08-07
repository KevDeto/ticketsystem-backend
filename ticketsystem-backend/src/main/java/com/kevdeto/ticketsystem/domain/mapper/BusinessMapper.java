package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kevdeto.ticketsystem.domain.dto.request.BusinessRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.BusinessResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "products", ignore = true)
	BusinessEntity toEntity(BusinessRequestDTO dto);

	@Mapping(target = "userId", source = "user.id")
	BusinessResponseDTO toResponse(BusinessEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "products", ignore = true)
	@Mapping(target = "user.id", source = "userId")
	void updateEntityFromRequest(BusinessRequestDTO dto, @MappingTarget BusinessEntity entity);
}
