package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kevdeto.ticketsystem.domain.dto.request.ProductRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	@Mapping(source = "business.id", target = "businessId")
	ProductResponseDTO toResponse(ProductEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(source = "businessId", target = "business")
	ProductEntity toEntity(ProductRequestDTO dto);

	default BusinessEntity mapBusinessIdToBusiness(Long businessId) {
		if (businessId == null)
			return null;
		BusinessEntity business = new BusinessEntity();
		business.setId(businessId);
		return business;
	}
}
