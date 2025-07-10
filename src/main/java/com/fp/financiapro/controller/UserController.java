package com.fp.financiapro.controller;

import com.fp.financiapro.entity.User;
import com.fp.financiapro.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // Création d'un utilisateur
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        log.info("Création d'un utilisateur: {}", request.getEmail());

        try {
            User user = userService.createUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getMonthlyIncome()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            log.error("Erreur création utilisateur: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Récupération d'un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupération par email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupération par API key
    @GetMapping("/api-key/{apiKey}")
    public ResponseEntity<User> getUserByApiKey(@PathVariable String apiKey) {
        Optional<User> user = userService.findByApiKey(apiKey);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Mise à jour d'un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody UpdateUserRequest request) {
        try {
            User updatedUser = userService.updateUser(
                    id,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getMonthlyIncome()
            );

            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.error("Erreur mise à jour utilisateur: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // DTOs pour les requêtes
    @lombok.Data
    public static class CreateUserRequest {
        private String firstName;
        private String lastName;
        private String email;
        private BigDecimal monthlyIncome;
    }

    @lombok.Data
    public static class UpdateUserRequest {
        private String firstName;
        private String lastName;
        private BigDecimal monthlyIncome;
    }
}
