package com.kevdeto.ticketsystem.auth.domain.dto;

import lombok.Getter;

@Getter
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
}
