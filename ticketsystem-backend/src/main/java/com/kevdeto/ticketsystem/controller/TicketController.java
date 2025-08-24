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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevdeto.ticketsystem.application.usecase.TicketUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.TicketRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.TicketResponseDTO;
import com.kevdeto.ticketsystem.domain.payload.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket", description = "Operaciones sobre tickets")
public class TicketController {

    private final TicketUseCase ticketUseCase;

    public TicketController(TicketUseCase ticketUseCase) {
        this.ticketUseCase = ticketUseCase;
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo ticket con ítems")
    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid TicketRequestDTO dto) {
        TicketResponseDTO created = ticketUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Ticket creado exitosamente", created, 201, LocalDateTime.now().toString(), null, "/api/tickets")
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Obtener un ticket por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
        TicketResponseDTO response = ticketUseCase.getById(id);
        return ResponseEntity.ok(
                new MessageResponse("Ticket encontrado", response, 200, LocalDateTime.now().toString(), null, "/api/tickets/" + id)
        );
    }

    // para comision: (listar tickets propios (falta implementar) si es user , todos si es admin)

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los tickets")
    @GetMapping
    public ResponseEntity<MessageResponse> getAll() {
        List<TicketResponseDTO> responseList = ticketUseCase.getAll();
        return ResponseEntity.ok(
                new MessageResponse("Listado de tickets", responseList, 200, LocalDateTime.now().toString(), null, "/api/tickets")
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un ticket por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        ticketUseCase.delete(id);
        return ResponseEntity.ok(
                new MessageResponse("Ticket eliminado correctamente", null, 200, LocalDateTime.now().toString(), null, "/api/tickets/" + id)
        );
    }
}