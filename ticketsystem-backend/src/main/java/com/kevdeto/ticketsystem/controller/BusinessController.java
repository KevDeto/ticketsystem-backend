package com.kevdeto.ticketsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.application.usecase.BusinessUseCase;
import com.kevdeto.ticketsystem.application.usecase.ProductUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.BusinessRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.BusinessResponseDTO;
import com.kevdeto.ticketsystem.domain.dto.response.PaginatedResponseDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;
import com.kevdeto.ticketsystem.domain.payload.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/businesses")
@Tag(name = "Business", description = "Operaciones sobre negocios")
public class BusinessController {

	private final BusinessUseCase businessUseCase;
	private final ProductUseCase productUseCase;

	public BusinessController(BusinessUseCase businessUseCase, ProductUseCase productUseCase) {
		this.businessUseCase = businessUseCase;
		this.productUseCase = productUseCase;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Crear un nuevo negocio")
	@PostMapping
	public ResponseEntity<MessageResponse> create(@RequestBody @Valid BusinessRequestDTO dto) {
		BusinessResponseDTO created = businessUseCase.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Negocio creado exitosamente",
				created, 201, LocalDateTime.now().toString(), null, "/api/businesses"));
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Obtener un negocio por ID")
	@GetMapping("/{id}")
	public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
		BusinessResponseDTO response = businessUseCase.getById(id);
		return ResponseEntity.ok(new MessageResponse("Negocio encontrado", response, 200,
				LocalDateTime.now().toString(), null, "/api/businesses/" + id));
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Listar todos los negocios")
	@GetMapping
	public ResponseEntity<MessageResponse> getAll() {
		List<BusinessResponseDTO> responseList = businessUseCase.getAll();
		return ResponseEntity.ok(new MessageResponse("Listado de negocios", responseList, 200,
				LocalDateTime.now().toString(), null, "/api/businesses"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Actualizar un negocio por ID")
	@PutMapping("/{id}")
	public ResponseEntity<MessageResponse> update(@PathVariable Long id, @RequestBody @Valid BusinessRequestDTO dto) {
		BusinessResponseDTO updated = businessUseCase.update(id, dto);
		return ResponseEntity.ok(new MessageResponse("Negocio actualizado", updated, 200,
				LocalDateTime.now().toString(), null, "/api/businesses/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Eliminar un negocio por ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
		businessUseCase.delete(id);
		return ResponseEntity.ok(new MessageResponse("Negocio eliminado correctamente", null, 200,
				LocalDateTime.now().toString(), null, "/api/businesses/" + id));
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Listar productos por negocio (paginado)")
	@GetMapping("/{id}/products")
	public ResponseEntity<MessageResponse> getProductsByBusinessId(@PathVariable Long id, @RequestParam int page,
			@RequestParam int size) {
		PaginatedResponseDTO<ProductResponseDTO> products = productUseCase.getByBusinessId(id, page, size);
		return ResponseEntity.ok(new MessageResponse("Productos del negocio", products, 200,
				LocalDateTime.now().toString(), null, "/api/business/" + id + "/products"));
	}
}
