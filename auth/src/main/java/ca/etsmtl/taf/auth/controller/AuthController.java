package ca.etsmtl.taf.auth.controller;

import ca.etsmtl.taf.auth.payload.request.LoginRequest;
import ca.etsmtl.taf.auth.payload.request.RefreshTokenRequest;
import ca.etsmtl.taf.auth.payload.request.SignupRequest;
import ca.etsmtl.taf.auth.payload.request.ValidateTokenRequest;
import ca.etsmtl.taf.auth.payload.response.JwtResponse;
import ca.etsmtl.taf.auth.payload.response.MessageResponse;
import ca.etsmtl.taf.auth.services.JwtService;
import ca.etsmtl.taf.auth.services.UserOldService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/")
    public String greeting() {
        return "Hello from Auth Microservice!";
    }

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserOldService userService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody @Valid LoginRequest authenticationRequest) throws Exception {
        final JwtResponse response = this.jwtService.createJwtToken(authenticationRequest);
        log.debug("Response {}", response.getFullName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody @Valid ValidateTokenRequest validateTokenRequest) throws Exception {
        final boolean response = this.jwtService.validateJwtToken(validateTokenRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> validateToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) throws Exception {
        try {
            final JwtResponse response = this.jwtService.refreshJwtToken(refreshTokenRequest);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException err) {
            return ResponseEntity.status(err.getStatusCode()).body(err.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if (this.userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        if (this.userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        this.userService.save(request);

        return ResponseEntity.ok(new MessageResponse("Inscription Réussie.!"));
    }

}
