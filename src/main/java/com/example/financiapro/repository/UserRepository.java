package com.example.financiapro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.financiapro.entity.User;
import com.example.financiapro.entity.BudgetItem;
import com.example.financiapro.entity.LoanRequest;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Recherche basique
    Optional<User> findByEmail(String email);

    Optional<User> findByApiKey(String apiKey);

    boolean existsByEmail(String email);

    boolean existsByApiKey(String apiKey);

    // Recherche avancée avec @Query
    @Query("SELECT u FROM User u WHERE u.nom LIKE %:nom% OR u.prenom LIKE %:prenom%")
    List<User> findByNomOrPrenomContaining(@Param("nom") String nom, @Param("prenom") String prenom);

    // Stats utilisateur
    @Query("SELECT COUNT(b) FROM BudgetItem b WHERE b.user.id = :userId")
    Long countBudgetItemsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(l) FROM LoanRequest l WHERE l.borrower.id = :userId OR l.lender.id = :userId")
    Long countLoansByUserId(@Param("userId") Long userId);
}
