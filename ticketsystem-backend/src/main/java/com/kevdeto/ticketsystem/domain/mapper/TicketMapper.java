package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kevdeto.ticketsystem.domain.dto.request.TicketRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketEntity;

@Mapper(componentModel = "spring", uses = { TicketItemMapper.class })
public interface TicketMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", source = "businessId")
	@Mapping(target = "items", source = "items")
	@Mapping(target = "total", ignore = true)
	TicketEntity toEntity(TicketRequestDTO dto);

	@Mapping(target = "businessId", source = "business.id")
	@Mapping(target = "items", source = "items")
	TicketResponseDTO toResponse(TicketEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", source = "businessId")
	@Mapping(target = "items", ignore = true)
	@Mapping(target = "total", ignore = true)
	void updateEntityFromRequest(TicketRequestDTO dto, @MappingTarget TicketEntity entity);

	default BusinessEntity businessFromId(Long id) {
		if (id == null)
			return null;
		BusinessEntity business = new BusinessEntity();
		business.setId(id);
		return business;
	}
}
