package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.JwtAuthenticationResponse;
import com.example.demo.exception.RegistrationException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import com.example.demo.service.AuthenticatedUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Autenticación", description = "Login y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con usuario y contraseña")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        AuthenticatedUser usuario = authService.autenticar(username, password);

        if (usuario == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        String accessToken = jwtTokenProvider.generateToken(usuario.getId(), usuario.getRol(), username);
        String refreshToken = jwtTokenProvider.generateRefreshToken(usuario.getId(), usuario.getRol(), username);

        JwtAuthenticationResponse response = JwtAuthenticationResponse.of(
            accessToken,
            refreshToken,
            usuario.getId(),
            usuario.getRol(),
            username,
            jwtExpirationInMs
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nueva cuenta de cliente")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        try {
            authService.registrar(
                data.get("nombre"),
                data.get("usuario"),
                data.get("email"),
                data.get("contrasena"),
                data.get("contrasenaConfirm")
            );
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta creada exitosamente");
            return ResponseEntity.ok(response);
        } catch (RegistrationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión actual")
    public ResponseEntity<?> logout() {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Sesión cerrada correctamente");
        return ResponseEntity.ok(body);
    }
}

