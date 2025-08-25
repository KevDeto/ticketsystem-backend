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
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.application.usecase.ProductUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.ProductRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.ProductResponseDTO;
import com.kevdeto.ticketsystem.domain.payload.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Operaciones sobre productos")
public class ProductController {

    private final ProductUseCase productUseCase;
    
    public ProductController(ProductUseCase productUseCase) {
    	this.productUseCase = productUseCase;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO created = productUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Producto creado exitosamente", created, 201, LocalDateTime.now().toString(), null, "/api/products")
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
        ProductResponseDTO response = productUseCase.getById(id);
        return ResponseEntity.ok(
                new MessageResponse("Producto encontrado", response, 200, LocalDateTime.now().toString(), null, "/api/products/" + id)
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<MessageResponse> getAll() {
        List<ProductResponseDTO> responseList = productUseCase.getAll();
        return ResponseEntity.ok(
                new MessageResponse("Listado de productos", responseList, 200, LocalDateTime.now().toString(), null, "/api/products")
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Actualizar un producto por ID")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO updated = productUseCase.update(id, dto);
        return ResponseEntity.ok(
                new MessageResponse("Producto actualizado", updated, 200, LocalDateTime.now().toString(), null, "/api/products/" + id)
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Eliminar un producto por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        productUseCase.delete(id);
        return ResponseEntity.ok(
                new MessageResponse("Producto eliminado correctamente", null, 200, LocalDateTime.now().toString(), null, "/api/products/" + id)
        );
    }
}