package com.kevdeto.ticketsystem.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevdeto.ticketsystem.domain.entity.BusinessEntity;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
	// Para: businessSecurity.isOwner()
//	boolean existsByIdAndUserId(Long id, Long ownerId);

	// Para: businessSecurity.belongsToBusiness()
//	boolean existsByIdAndUsers_Id(Long id, Long userId);
	// Verifica si el negocio 'id' tiene un usuario con ID 'userId'
	// en su lista de empleados (users)
	// Nota: La nomenclatura `Users_Id` sigue las reglas de Spring Data para buscar
	// por la propiedad `id` de la entidad dentro de la colección `users`.
}
