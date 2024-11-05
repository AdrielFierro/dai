package com.tpo.TPO.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.tpo.TPO.controller.dto.JwtResponse;
import com.tpo.TPO.controller.dto.RefreshTokenRequest;
import com.tpo.TPO.repository.UserRepository;
import com.tpo.TPO.repository.RefreshTokenRepository;
import com.tpo.TPO.service.AuthenticationService;
import com.tpo.TPO.service.RefreshTokenService;
import com.tpo.TPO.service.UserService;
import com.tpo.TPO.controller.config.JwtService;
import com.tpo.TPO.entity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository RefreshTokenRepository;

    @Autowired
    private JwtService jwtservice;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        Optional<com.tpo.TPO.entity.User> user = userRepository.findByUsername(request.getUsername());
        Optional<com.tpo.TPO.entity.User> usermail = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {

            if (usermail.isEmpty()) {
                return ResponseEntity.ok(service.register(request));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail existente");

            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario existente");
        }

    }
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody AuthenticationRequest request) {
        String usernameString = request.getUsername();
        
        System.out.println("Username recibido: " + request.getUsername());
        System.out.println("Password recibido: " + request.getPassword());
        // Buscar el usuario en la base de datos
        Optional<User> user = userRepository.findByUsername(usernameString);
        
        // Verificar si el usuario existe
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        
        User realuser = user.get();
        int iduser = realuser.getId();

        System.out.println("Username REAL: " + realuser);
        System.out.println("ID REAL: " + iduser);

        Optional<RefreshToken> refreshTokenSearched = RefreshTokenRepository.findByUser_id(iduser);

        RefreshToken refreshToken;
        if (refreshTokenSearched.isPresent()) {
            refreshToken = refreshTokenSearched.get(); // Aquí accedes al valor de forma segura
        } else {
            // Si no existe el refresh token, creamos uno nuevo
            refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
        }

        return JwtResponse.builder()
            .accessToken(service.authenticate(request))
            .token(refreshToken.getToken()) // Usamos el refreshToken que existe o el nuevo
            .build();
    }


    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(User -> {
                    String accessToken = jwtservice.generateToken(User, User.getId());
                    return JwtResponse.builder().accessToken(accessToken).token(refreshTokenRequest.getToken())
                            .build();

                }).orElseThrow(() -> new RuntimeException("Token not in DB"));

    }

    // Only for testing
    @PostMapping("/findToken")
    public Optional<RefreshToken> findToken(@RequestBody AuthenticationRequest authenticationRequest) {

        String usernameString = authenticationRequest.getUsername();
        Optional<User> user = userRepository.findByUsername(usernameString);
        User realuser = user.get();
        int iduser = realuser.getId();

        return RefreshTokenRepository.findByUser_id(iduser);

    }

}