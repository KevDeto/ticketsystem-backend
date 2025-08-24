package com.kevdeto.ticketsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.application.usecase.TicketItemUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.TicketItemRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketItemResponseDTO;
import com.kevdeto.ticketsystem.domain.payload.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ticket-items")
@Tag(name = "TicketItem", description = "Operaciones sobre ítems de tickets")
public class TicketItemController {

	private final TicketItemUseCase ticketItemUseCase;

	public TicketItemController(TicketItemUseCase ticketItemUseCase) {
		this.ticketItemUseCase = ticketItemUseCase;
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@Operation(summary = "Obtener un ítem de ticket por ID")
	@GetMapping("/{id}")
	public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
		TicketItemResponseDTO response = ticketItemUseCase.getById(id);
		return ResponseEntity.ok(new MessageResponse("Ítem encontrado", response, 200, LocalDateTime.now().toString(),
				null, "/api/ticket-items/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Listar todos los ítems de tickets")
	@GetMapping
	public ResponseEntity<MessageResponse> getAll() {
		List<TicketItemResponseDTO> responseList = ticketItemUseCase.getAll();
		return ResponseEntity.ok(new MessageResponse("Listado de ítems", responseList, 200,
				LocalDateTime.now().toString(), null, "/api/ticket-items"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Actualizar un ítem de ticket")
	@PutMapping("/{id}")
	public ResponseEntity<MessageResponse> update(@PathVariable Long id, @RequestBody @Valid TicketItemRequestDTO dto) {
		TicketItemResponseDTO updated = ticketItemUseCase.update(id, dto);
		return ResponseEntity.ok(new MessageResponse("Ítem actualizado correctamente", updated, 200,
				LocalDateTime.now().toString(), null, "/api/ticket-items/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Eliminar un ítem de ticket")
	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
		ticketItemUseCase.delete(id);
		return ResponseEntity.ok(new MessageResponse("Ítem eliminado correctamente", null, 200,
				LocalDateTime.now().toString(), null, "/api/ticket-items/" + id));
	}
}