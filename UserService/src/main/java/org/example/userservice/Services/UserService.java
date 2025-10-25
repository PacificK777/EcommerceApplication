package org.example.userservice.Services;

import org.example.userservice.Exceptions.InvalidPasswordException;
import org.example.userservice.Exceptions.InvalidTokenException;
import org.example.userservice.Exceptions.UserAlreadyExistsException;
import org.example.userservice.Exceptions.UserNotFoundException;
import org.example.userservice.Models.Tokens;
import org.example.userservice.Models.User;

public interface UserService {
    User signup(String name, String email, String password) throws UserAlreadyExistsException;
    Tokens login(String email, String password) throws UserNotFoundException, InvalidPasswordException;
    User validateToken(String token) throws InvalidTokenException;
    void logout(String token) throws InvalidTokenException;

}
