package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kevdeto.ticketsystem.domain.dto.request.ProductRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", source = "businessId")
	ProductEntity toEntity(ProductRequestDTO dto);

	@Mapping(target = "businessId", source = "business.id")
	ProductResponseDTO toResponse(ProductEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "business", source = "businessId")
	void updateEntityFromRequest(ProductRequestDTO dto, @MappingTarget ProductEntity entity);

	// Esto me mapea de un ID de business a una entidad business
	default BusinessEntity businessFromId(Long id) {
		if (id == null)
			return null;
		BusinessEntity business = new BusinessEntity();
		business.setId(id);
		return business;
	}
}
