package com.kevdeto.ticketsystem.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketItemResponseDTO;
import com.kevdeto.ticketsystem.domain.entity.ProductEntity;
import com.kevdeto.ticketsystem.domain.entity.TicketItemEntity;

@Mapper(componentModel = "spring")
public interface TicketItemMapper {
	@Mapping(source = "product.id", target = "productId")
	TicketItemResponseDTO toResponse(TicketItemEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticket", ignore = true)
	@Mapping(source = "productId", target = "product")
	TicketItemEntity toEntity(TicketItemRequestDTO dto);

	default ProductEntity mapProductIdToProduct(Long productId) {
		if (productId == null)
			return null;
		ProductEntity product = new ProductEntity();
		product.setId(productId);
		return product;
	}
}
