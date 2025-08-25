package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.domain.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	Page<ProductEntity> findByBusinessId(Long businessId, Pageable pageable);

	// Para: businessSecurity.canAccessProduct()
//	boolean existsByIdAndBusiness_OwnerId(Long id, Long ownerId);
	// Verifica que el producto con 'id' exista y que el
	// owner de su negocio asociado sea 'ownerId'
	// La nomenclatura `Business_OwnerId` significa "busca por la propiedad
	// `ownerId` de la entidad `Business` asociada".
}
