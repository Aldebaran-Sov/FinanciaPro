package com.fp.financiapro.service;

import com.fp.financiapro.entity.User;
import com.fp.financiapro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // Création d'utilisateur
    public User createUser(String firstName, String lastName, String email, BigDecimal monthlyIncome) {
        log.info("Création d'un nouvel utilisateur: {}", email);

        // Vérification unicité email
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Génération API key unique
        String apiKey = generateUniqueApiKey();

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .monthlyIncome(monthlyIncome)
                .apiKey(apiKey)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Utilisateur créé avec succès: ID={}, Email={}", savedUser.getId(), savedUser.getEmail());

        return savedUser;
    }

    // Recherche par email
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Recherche par API key
    @Transactional(readOnly = true)
    public Optional<User> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    // Recherche par ID
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Mise à jour profil
    public User updateUser(Long userId, String firstName, String lastName, BigDecimal monthlyIncome) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMonthlyIncome(monthlyIncome);

        return userRepository.save(user);
    }

    // Logique métier : utilisateurs potentiels pour prêter
    @Transactional(readOnly = true)
    public List<User> findPotentialLenders(Long borrowerId, BigDecimal loanAmount) {
        // Seuil minimum : 3x le montant du prêt en revenus mensuels
        BigDecimal minIncomeThreshold = loanAmount.multiply(BigDecimal.valueOf(3));

        return userRepository.findPotentialLenders(minIncomeThreshold, borrowerId);
    }

    // Statistiques utilisateur
    @Transactional(readOnly = true)
    public long countUsersWithMinIncome(BigDecimal minIncome) {
        return userRepository.countUsersWithMinIncome(minIncome);
    }

    // Génération API key unique
    private String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = "fp_" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
        } while (userRepository.existsByApiKey(apiKey));

        return apiKey;
    }

    // Validation utilisateur actif
    @Transactional(readOnly = true)
    public boolean isValidUser(Long userId) {
        return userRepository.existsById(userId);
    }

    // Récupération sécurisée
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec ID: " + userId));
    }
}
