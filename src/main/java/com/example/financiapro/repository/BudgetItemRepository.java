package com.example.financiapro.repository;

import com.example.financiapro.entity.BudgetItem;
import com.example.financiapro.entity.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {

    // Recherche par utilisateur
    List<BudgetItem> findByUserId(Long userId);

    List<BudgetItem> findByUserIdOrderByDateDesc(Long userId);

    // Recherche par type
    List<BudgetItem> findByUserIdAndType(Long userId, BudgetType type);

    // Recherche par période
    List<BudgetItem> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Statistiques avec @Query
    @Query("SELECT SUM(b.montant) FROM BudgetItem b WHERE b.user.id = :userId AND b.type = :type")
    Optional<BigDecimal> sumMontantByUserIdAndType(@Param("userId") Long userId, @Param("type") BudgetType type);

    @Query("SELECT b.type, SUM(b.montant) FROM BudgetItem b WHERE b.user.id = :userId GROUP BY b.type")
    List<Object[]> getSummaryByUserIdGroupByType(@Param("userId") Long userId);

    // Top dépenses
    @Query("SELECT b FROM BudgetItem b WHERE b.user.id = :userId AND b.type = 'EXPENSE' ORDER BY b.montant DESC")
    List<BudgetItem> findTopExpensesByUserId(@Param("userId") Long userId, Pageable pageable);

    // Recherche textuelle
    @Query("SELECT b FROM BudgetItem b WHERE b.user.id = :userId AND LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BudgetItem> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}
