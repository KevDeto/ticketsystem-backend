package com.kevdeto.ticketsystem.auth.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
	private String username;
	private String password;
}
