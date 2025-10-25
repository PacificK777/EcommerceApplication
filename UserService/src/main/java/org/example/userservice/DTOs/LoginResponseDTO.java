package org.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.Models.Tokens;

@Getter
@Setter
public class LoginResponseDTO {
    private TokenDTO tokenDto;
    private String message;
}
