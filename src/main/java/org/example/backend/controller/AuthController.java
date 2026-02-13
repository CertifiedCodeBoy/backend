package org.example.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.request.LoginRequest;
import org.example.backend.dto.request.RegisterRequest;
import org.example.backend.dto.response.AuthResponse;
import org.example.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    public ResponseEntity<org.example.backend.dto.response.ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("Login request received for: {}", request.getLogin());

        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(
                org.example.backend.dto.response.ApiResponse.success(authResponse, "Login successful"));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    public ResponseEntity<org.example.backend.dto.response.ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        log.info("Registration request received for: {}", request.getUsername());

        AuthResponse authResponse = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(org.example.backend.dto.response.ApiResponse.success(authResponse, "Registration successful"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<org.example.backend.dto.response.ApiResponse<AuthResponse>> refreshToken(
            @RequestParam String refreshToken) {

        log.debug("Token refresh request received");

        AuthResponse authResponse = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(
                org.example.backend.dto.response.ApiResponse.success(authResponse, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<org.example.backend.dto.response.ApiResponse<String>> logout(
            @RequestHeader("Authorization") String token) {

        log.info("Logout request received");

        // Extract token from "Bearer " prefix
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        authService.logout(jwt);

        return ResponseEntity.ok(
                org.example.backend.dto.response.ApiResponse.success("Logout successful"));
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint for checking if auth endpoints are working")
    public ResponseEntity<org.example.backend.dto.response.ApiResponse<String>> test() {
        return ResponseEntity.ok(
                org.example.backend.dto.response.ApiResponse.success("Auth endpoints are working!",
                        "Authentication system is operational"));
    }
}
