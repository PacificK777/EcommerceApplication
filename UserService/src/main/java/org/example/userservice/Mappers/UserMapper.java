package org.example.userservice.Mappers;

import org.example.userservice.DTOs.SignUpResponseDTO;
import org.example.userservice.DTOs.TokenDTO;
import org.example.userservice.DTOs.UserDTO;
import org.example.userservice.Models.Tokens;
import org.example.userservice.Models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public SignUpResponseDTO toSignUpResponse(User user) {
        SignUpResponseDTO dto = new SignUpResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setMessage("User created successfully");
        return dto;
    }

    public TokenDTO toTokenDTO(Tokens token) {
        TokenDTO dto = new TokenDTO();
        dto.setTokenValue(token.getTokenValue());
        dto.setExpiryAt(token.getExpiryAt());
        if (token.getUser() != null) {
            dto.setEmail(token.getUser().getEmail());
        }
        return dto;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }
}

