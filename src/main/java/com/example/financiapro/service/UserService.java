package com.example.financiapro.service;

// Imports Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Imports Java standard
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Imports de tes entités
import com.example.financiapro.entity.User;
import com.example.financiapro.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==================== CRUD DE BASE ====================

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public User createUser(User user) {
        validateUserData(user);
        // Générer clé API unique si pas fournie
        if (user.getApiKey() == null || user.getApiKey().isEmpty()) {
            user.setApiKey(generateUniqueApiKey());
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNom(updatedUser.getNom());
                    existingUser.setPrenom(updatedUser.getPrenom());
                    existingUser.setEmail(updatedUser.getEmail());
                    // Ne pas modifier l'API key lors d'un update classique
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + id);
        }
        userRepository.deleteById(id);
    }

    // ==================== AUTHENTIFICATION ====================

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ==================== RECHERCHE AVANCÉE ====================

    @Transactional(readOnly = true)
    public List<User> searchByNomOrPrenom(String searchTerm) {
        return userRepository.findByNomOrPrenomContaining(searchTerm, searchTerm);
    }

    // ==================== STATISTIQUES ====================

    @Transactional(readOnly = true)
    public Long getBudgetItemsCount(Long userId) {
        return userRepository.countBudgetItemsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Long getLoansCount(Long userId) {
        return userRepository.countLoansByUserId(userId);
    }

    // ==================== GESTION API KEY ====================

    public User regenerateApiKey(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setApiKey(generateUniqueApiKey());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));
    }

    // ==================== VALIDATION & UTILITAIRES ====================

    private void validateUserData(User user) {
        if (user.getEmail() != null && existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà : " + user.getEmail());
        }
        if (user.getApiKey() != null && userRepository.existsByApiKey(user.getApiKey())) {
            throw new RuntimeException("Cette clé API existe déjà");
        }
    }

    private String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = "fp_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        } while (userRepository.existsByApiKey(apiKey));
        return apiKey;
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
