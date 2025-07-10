package com.fp.financiapro.repository;

import com.fp.financiapro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Requêtes simples par convention Spring Data
    Optional<User> findByEmail(String email);
    Optional<User> findByApiKey(String apiKey);
    boolean existsByEmail(String email);
    boolean existsByApiKey(String apiKey);

    // Requêtes métier personnalisées
    @Query("SELECT u FROM User u WHERE u.monthlyIncome >= :minIncome")
    List<User> findUsersWithMinIncome(@Param("minIncome") BigDecimal minIncome);

    @Query("SELECT u FROM User u WHERE u.monthlyIncome BETWEEN :minIncome AND :maxIncome")
    List<User> findUsersByIncomeRange(@Param("minIncome") BigDecimal minIncome,
                                      @Param("maxIncome") BigDecimal maxIncome);

    // Statistiques pour le matching de prêts
    @Query("SELECT COUNT(u) FROM User u WHERE u.monthlyIncome >= :minIncome")
    long countUsersWithMinIncome(@Param("minIncome") BigDecimal minIncome);

    // Utilisateurs potentiels pour prêter (revenus > seuil)
    @Query("SELECT u FROM User u WHERE u.monthlyIncome >= :threshold AND u.id != :excludeUserId")
    List<User> findPotentialLenders(@Param("threshold") BigDecimal threshold,
                                    @Param("excludeUserId") Long excludeUserId);
}
