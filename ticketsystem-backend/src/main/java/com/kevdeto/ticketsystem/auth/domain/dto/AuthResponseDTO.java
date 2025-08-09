package com.kevdeto.ticketsystem.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
	private String message;
    private String token;
    private String refreshToken;
}
