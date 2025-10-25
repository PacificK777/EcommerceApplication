package org.example.userservice.Services;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.userservice.Exceptions.InvalidPasswordException;
import org.example.userservice.Exceptions.InvalidTokenException;
import org.example.userservice.Exceptions.UserAlreadyExistsException;
import org.example.userservice.Exceptions.UserNotFoundException;
import org.example.userservice.Models.Tokens;
import org.example.userservice.Models.User;
import org.example.userservice.Repository.TokenRepository;
import org.example.userservice.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           TokenRepository tokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User signup(String name, String email, String password) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setIsDeleted(false);
        return userRepository.save(user);
    }

    @Override
    public Tokens login(String email, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email id : " + email + " not found!");
        } else {
            User user = optionalUser.get();
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                throw new InvalidPasswordException("Invalid password");
            }
            Tokens token = createToken(user);
            return tokenRepository.save(token);
        }
    }

    private Tokens createToken(User user) {
        Tokens tokens = new Tokens();
        tokens.setUser(user);
        tokens.setIsDeleted(false);
        tokens.setExpiryAt(java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(28)));
        tokens.setTokenValue(RandomStringUtils.insecure().nextAlphabetic(50));
        return tokens;
    }

    @Override
    public User validateToken(String token) throws InvalidTokenException {
        Optional<Tokens> optionalTokens = tokenRepository.
                findByTokenValueAndIsDeletedAndExpiryAtGreaterThan(
                        token,
                        false,
                        new java.util.Date());
        if (optionalTokens.isEmpty()) {
            throw new InvalidTokenException("Invalid or expired token!");
        }
        return optionalTokens.get().getUser();
    }

    @Override
    public void logout(String token) throws InvalidTokenException {
        Optional<Tokens> optionalTokens = tokenRepository.
                findByTokenValueAndIsDeletedAndExpiryAtGreaterThan(
                        token,
                        false,
                        new java.util.Date());
        if (optionalTokens.isEmpty()) {
            throw new InvalidTokenException("Invalid or expired token!");
        }
        Tokens tokens = optionalTokens.get();
        tokens.setIsDeleted(true);
        tokenRepository.save(tokens);
    }
}
