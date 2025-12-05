package com.ace.authservice.controller;




import com.ace.authservice.dto.AuthResponse;
import com.ace.authservice.dto.LoginRequest;
import com.ace.authservice.dto.RegisterRequest;
import com.ace.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        String message = authService.verifyEmail(token); // ex: "Compte vérifié avec succès..."

        // On redirige vers le front avec le message en paramètre
        String url = "http://localhost:4200/login?verified=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setLocation(java.net.URI.create(url));

        return new ResponseEntity<>(headers, org.springframework.http.HttpStatus.FOUND); // 302 redirect
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/test")
    public String test() {
        return "Backend Auth-Service OK ";
    }

    @PostMapping("/create-admin")
    public String createAdmin() {
        return authService.createAdmin();
    }


}

