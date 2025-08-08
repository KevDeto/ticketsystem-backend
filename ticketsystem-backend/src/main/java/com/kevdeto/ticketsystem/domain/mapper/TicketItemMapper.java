package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketItemResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;

@Mapper(componentModel = "spring")
public interface TicketItemMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticket", ignore = true)
	@Mapping(target = "subtotal", ignore = true)
	@Mapping(target = "product", source = "productId")
	TicketItemEntity toEntity(TicketItemRequestDTO dto);

	@Mapping(target = "productId", source = "product.id")
	TicketItemResponseDTO toResponse(TicketItemEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticket", ignore = true)
	@Mapping(target = "subtotal", ignore = true)
	@Mapping(target = "product", source = "productId")
	void updateEntityFromRequest(TicketItemRequestDTO dto, @MappingTarget TicketItemEntity entity);

	default ProductEntity productFromId(Long id) {
		if (id == null)
			return null;
		ProductEntity product = new ProductEntity();
		product.setId(id);
		return product;
	}
}
