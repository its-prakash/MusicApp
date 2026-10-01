package com.musicBackend.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        return userRepository.findByEmailOrUserName(authRequest.getEmail(), authRequest.getUserName())
                .flatMap(existingUser -> {
                    if (existingUser.getEmail().equalsIgnoreCase(authRequest.getEmail())) {
                        return Mono.error(new RuntimeException("Email already exists"));
                    } else {
                        return Mono.error(new RuntimeException("Username already exists"));
                    }
                })
                .cast(User.class)
                .switchIfEmpty(Mono.defer(() -> {
                    User newUser = new User();
                    newUser.setUserName(authRequest.getUserName());
                    newUser.setEmail(authRequest.getEmail());
                    newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
                    newUser.setRole("ROLE_USER");
                    return userRepository.save(newUser);
                }))
                .map(savedUser -> {
                    String token = jwtUtils.generateToken(savedUser.getUserName());
                    return new AuthResponse(token, savedUser.getUserName());
                });
    }

    public Mono<AuthResponse> login(AuthRequest authRequest) {
        // Extract whichever field the client provided as the login identifier
        String identifier = authRequest.getEmail() != null && !authRequest.getEmail().isBlank()
                ? authRequest.getEmail()
                : authRequest.getUserName();

        if (identifier == null || identifier.isBlank()) {
            return Mono.error(new RuntimeException("Username or email must be provided"));
        }

        // Search both email and username columns against the identifier
        return userRepository.findByEmailOrUserName(identifier, identifier)
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid username, email, or password")))
                .flatMap(user -> {

                    if (tokenBlacklistService.hasActiveSession(user.getUserName())) {
                        return Mono.error(new RuntimeException("User is already logged in. Please log out first."));
                    }

                    if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid username, email, or password"));
                    }

                    String token = jwtUtils.generateToken(user.getUserName());
                    tokenBlacklistService.registerActiveSession(user.getUserName(), token);
                    return Mono.just(new AuthResponse(token, user.getUserName()));
                });
    }

    public Mono<String> logout(String authHeader){
        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(!jwtUtils.validateToken(token)){
                return Mono.error(new RuntimeException("Invalid or expired token"));
            }

            String userName = jwtUtils.getUsernameFromToken(token);

            tokenBlacklistService.removeAccountSession(userName, token);
            return Mono.just("Logged out successfully");
        }
        return Mono.error(new RuntimeException("Invalid Authorization header"));
    }
}