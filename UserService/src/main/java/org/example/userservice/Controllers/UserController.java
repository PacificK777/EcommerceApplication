package org.example.userservice.Controllers;

import org.example.userservice.DTOs.*;
import org.example.userservice.Exceptions.InvalidPasswordException;
import org.example.userservice.Exceptions.InvalidTokenException;
import org.example.userservice.Exceptions.UserAlreadyExistsException;
import org.example.userservice.Exceptions.UserNotFoundException;
import org.example.userservice.Models.Tokens;
import org.example.userservice.Models.User;
import org.example.userservice.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO requestDTO) throws UserAlreadyExistsException {
        User user = userService.signup(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getPassword());

        SignUpResponseDTO responseDTO = new SignUpResponseDTO();
        HttpStatus status;

        if (user != null) {
            responseDTO.setEmail(user.getEmail());
            responseDTO.setName(user.getName());
            responseDTO.setMessage("User created successfully");
            status = HttpStatus.OK;
        } else {
            responseDTO.setMessage("User creation failed");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(responseDTO, status);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) throws UserNotFoundException, InvalidPasswordException {
        Tokens token = userService.login(
                requestDTO.getEmail(),
                requestDTO.getPassword()
        );
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        HttpStatus status;
        if (token != null) {
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setTokenValue(token.getTokenValue());
            tokenDTO.setExpiryAt(token.getExpiryAt());
            if (token.getUser() != null) {
                tokenDTO.setEmail(token.getUser().getEmail());
            }

            responseDTO.setTokenDto(tokenDTO);
            responseDTO.setMessage("Login Successful");
            status = HttpStatus.OK;
        } else {
            responseDTO.setMessage("Login failed");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseDTO, status);
    }


    @PostMapping("/validate")
    public ResponseEntity<UserDTO> validateToken(@RequestHeader("Authorization") String token) throws InvalidTokenException {
        User user = userService.validateToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogOutResponseDTO> logout(@RequestHeader("Authorization") String token) {
        LogOutResponseDTO responseDTO = new LogOutResponseDTO();
        HttpStatus status;
        try {
            userService.logout(token);
            responseDTO.setMessage("Logout successful");
            status = HttpStatus.OK;
        } catch (InvalidTokenException e) {
            responseDTO.setMessage(e.getMessage());
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(responseDTO, status);
    }
}
