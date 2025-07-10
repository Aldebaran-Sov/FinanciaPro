package com.fp.financiapro.repository;

import com.fp.financiapro.entity.BudgetItem;
import com.fp.financiapro.entity.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {

    // Requêtes par utilisateur
    List<BudgetItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<BudgetItem> findByUserIdAndType(Long userId, BudgetType type);

    // Requêtes par période
    @Query("SELECT b FROM BudgetItem b WHERE b.user.id = :userId AND b.createdAt BETWEEN :start AND :end")
    List<BudgetItem> findByUserIdAndDateRange(@Param("userId") Long userId,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    // Calculs financiers
    @Query("SELECT SUM(b.amount) FROM BudgetItem b WHERE b.user.id = :userId AND b.type = :type")
    BigDecimal calculateTotalByUserAndType(@Param("userId") Long userId,
                                           @Param("type") BudgetType type);

    @Query("SELECT SUM(b.amount) FROM BudgetItem b WHERE b.user.id = :userId AND b.type = :type AND b.createdAt BETWEEN :start AND :end")
    BigDecimal calculateTotalByUserTypeAndDateRange(@Param("userId") Long userId,
                                                    @Param("type") BudgetType type,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    // Balance mensuelle (revenus - dépenses)
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN b.type = 'INCOME' THEN b.amount ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN b.type = 'EXPENSE' THEN b.amount ELSE 0 END), 0) " +
            "FROM BudgetItem b WHERE b.user.id = :userId AND b.createdAt BETWEEN :start AND :end")
    BigDecimal calculateMonthlyBalance(@Param("userId") Long userId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    // Top catégories de dépenses
    @Query("SELECT b.description, SUM(b.amount) FROM BudgetItem b " +
            "WHERE b.user.id = :userId AND b.type = 'EXPENSE' " +
            "GROUP BY b.description ORDER BY SUM(b.amount) DESC")
    List<Object[]> findTopExpensesByUser(@Param("userId") Long userId);
}
