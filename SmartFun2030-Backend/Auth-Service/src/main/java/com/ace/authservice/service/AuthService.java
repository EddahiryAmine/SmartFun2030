package com.ace.authservice.service;





import com.ace.authservice.dto.AuthResponse;
import com.ace.authservice.dto.LoginRequest;
import com.ace.authservice.dto.RegisterRequest;
import com.ace.authservice.model.User;
import com.ace.authservice.repository.UserRepository;
import com.ace.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final JwtService jwtService;


    // ---------------- REGISTER ----------------
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Générer un token unique
        String token = UUID.randomUUID().toString();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .country(request.getCountry())
                .roles(Set.of("USER"))
                .verified(false)
                .verificationToken(token)
                .build();

        userRepository.save(user);

        String link = "http://localhost:8081/api/auth/verify?token=" + token;

        mailSenderService.sendVerificationEmail(
                request.getEmail(),
                request.getFirstName(),
                link
        );


        return "Compte créé. Vérifiez votre email pour l’activer.";
    }


    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public String createAdmin() {

        if (userRepository.existsByEmail("admin@smartfun.com")) {
            return "Admin déjà existant";
        }

        User admin = User.builder()
                .email("admin@smartfun.com")
                .password(passwordEncoder.encode("admin123")) // mot de passe admin
                .firstName("Super")
                .lastName("Admin")
                .country("Global")
                .roles(Set.of("ADMIN"))
                .verified(true)
                .verificationToken(null)
                .build();

        userRepository.save(admin);

        return "Admin créé avec succès ! Email : admin@smartfun.com / Password : admin123";
    }



    // ---------------- VERIFY TOKEN ----------------
    public String verifyEmail(String token) {

        User user = userRepository.findByVerificationToken(token).orElse(null);

        if (user == null) {
            return "Lien invalide ou expiré.";
        }

        if (user.isVerified()) {
            return "Ce compte est déjà activé.";
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Compte vérifié avec succès. Vous pouvez maintenant vous connecter.";
    }


    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe invalide"));

        if (!user.isVerified()) {
            throw new RuntimeException("Veuillez vérifier votre email avant de vous connecter.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe invalide");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        return new AuthResponse(token);
    }
}

