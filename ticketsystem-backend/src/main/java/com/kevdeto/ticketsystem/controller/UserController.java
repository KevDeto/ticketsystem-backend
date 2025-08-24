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

import com.kevdeto.ticketsystem.application.usecase.UserUseCase;
import com.kevdeto.ticketsystem.domain.dto.request.UserRequestDTO;
import com.kevdeto.ticketsystem.domain.dto.response.UserResponseDTO;
import com.kevdeto.ticketsystem.domain.payload.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Operaciones sobre usuarios")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO created = userUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Usuario creado correctamente", created, 201, LocalDateTime.now().toString(), null, "/api/users")
        );
    }
    // un usuario debe consultar su propio perfil y un admin puede consultar cualquier perfil
	@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
        UserResponseDTO user = userUseCase.getById(id);
        return ResponseEntity.ok(
                new MessageResponse("Usuario encontrado", user, 200, LocalDateTime.now().toString(), null, "/api/users/" + id)
        );
    }

	@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<MessageResponse> getAll() {
        List<UserResponseDTO> users = userUseCase.getAll();
        return ResponseEntity.ok(
                new MessageResponse("Listado de usuarios", users, 200, LocalDateTime.now().toString(), null, "/api/users")
        );
    }
	//un admin puede actualizar cualquiera pero un usuario debe actualizar su propio perfil
	@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO updated = userUseCase.update(id, dto);
        return ResponseEntity.ok(
                new MessageResponse("Usuario actualizado correctamente", updated, 200, LocalDateTime.now().toString(), null, "/api/users/" + id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        userUseCase.delete(id);
        return ResponseEntity.ok(
                new MessageResponse("Usuario eliminado correctamente", null, 200, LocalDateTime.now().toString(), null, "/api/users/" + id)
        );
    }
}
