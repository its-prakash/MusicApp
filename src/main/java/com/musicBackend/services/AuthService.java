package com.musicBackend.services;

import com.musicBackend.dtos.AuthRequest;
import com.musicBackend.dtos.AuthResponse;
import com.musicBackend.entity.User;
import com.musicBackend.repository.UserRepository;
import com.musicBackend.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class AuthService  {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        return userRepository.findByUserName(authRequest.getUserName())
                .flatMap(existingUser -> Mono.<User>error(new RuntimeException("Username already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    User newUser = new User();
                    newUser.setUserName(authRequest.getUserName());

                    newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
                    newUser.setRole("ROLE_USER");
                    return userRepository.save(newUser);
                }))
                .map(savedUser -> {
                    String token = jwtUtils.generateToken(savedUser.getUserName());
                    return new AuthResponse(token, savedUser.getUserName());
                });
    }

    public Mono<AuthResponse> login(AuthRequest authRequest){
        return userRepository.findByUserName(authRequest.getUserName()).switchIfEmpty(Mono.error(new RuntimeException("Invalid username or password"))).flatMap(user -> {
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return Mono.error(new RuntimeException("Invalid username or password"));
            }

            String token = jwtUtils.generateToken(user.getUserName());
            return Mono.just(new AuthResponse(token, user.getUserName()));
        });
    }
}
