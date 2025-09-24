package org.azgl.lbms.security;

import org.azgl.lbms.dto.AuthenticationRequest;
import org.azgl.lbms.dto.AuthenticationResponse;
import org.azgl.lbms.model.Role;
import org.azgl.lbms.model.User;
import org.azgl.lbms.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalStateException("Username is taken");
        }

        user.setRole(Role.READER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saveduser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(saveduser);

        return new AuthenticationResponse(jwtToken,saveduser.getUsername(),saveduser.getRole().name());
    }

    public AuthenticationResponse login(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalStateException("Authentication failed: User not found after successful verification"));
        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken, user.getUsername(), user.getRole().name());
    }
}
