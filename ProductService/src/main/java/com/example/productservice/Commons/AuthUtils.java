package com.example.productservice.Commons;

import com.example.productservice.DTOs.UserDTO;
import com.example.productservice.Exceptions.InvalidTokenException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthUtils {
    private final RestTemplate restTemplate;

    public AuthUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO validateTokens(String tokenValue) throws InvalidTokenException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenValue);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserDTO> response = restTemplate.postForEntity("http://localhost:5050/users/validate", entity, UserDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // If token is invalid, throw a custom exception
            throw new InvalidTokenException("Invalid or expired token.");
        }
    }
}
