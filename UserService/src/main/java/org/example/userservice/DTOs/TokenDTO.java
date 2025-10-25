package org.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenDTO {
    private String email;
    private String tokenValue;
    private Date expiryAt;
}
