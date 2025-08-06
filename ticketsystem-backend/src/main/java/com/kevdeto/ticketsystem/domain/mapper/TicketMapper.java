package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kevdeto.ticketsystem.domain.dto.request.TicketRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketEntity;

@Mapper(componentModel = "spring", uses = {TicketItemMapper.class})
public interface TicketMapper {
	@Mapping(source = "business.id", target = "businessId")
    TicketResponseDTO toResponse(TicketEntity entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "businessId", target = "business")
    TicketEntity toEntity(TicketRequestDTO dto);
	
    default BusinessEntity mapBusinessIdToBusiness(Long businessId) {
        if (businessId == null) {
            return null;
        }
        BusinessEntity business = new BusinessEntity();
        business.setId(businessId);
        return business;
    }
}
