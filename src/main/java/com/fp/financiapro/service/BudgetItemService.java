package com.fp.financiapro.service;

import com.fp.financiapro.entity.BudgetItem;
import com.fp.financiapro.entity.BudgetType;
import com.fp.financiapro.entity.User;
import com.fp.financiapro.repository.BudgetItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;
    private final UserService userService;

    // Création d'un item budgétaire
    public BudgetItem createBudgetItem(Long userId, String description, BigDecimal amount, BudgetType type) {
        log.info("Création d'un item budgétaire pour l'utilisateur {}: {} - {}", userId, description, amount);

        User user = userService.getUserById(userId);

        BudgetItem budgetItem = BudgetItem.builder()
                .description(description)
                .amount(amount)
                .type(type)
                .user(user)
                .build();

        BudgetItem savedItem = budgetItemRepository.save(budgetItem);
        log.info("Item budgétaire créé avec succès: ID={}", savedItem.getId());

        return savedItem;
    }

    // Récupération des items par utilisateur
    @Transactional(readOnly = true)
    public List<BudgetItem> getBudgetItemsByUser(Long userId) {
        return budgetItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Récupération par type
    @Transactional(readOnly = true)
    public List<BudgetItem> getBudgetItemsByUserAndType(Long userId, BudgetType type) {
        return budgetItemRepository.findByUserIdAndType(userId, type);
    }

    // Calcul du total par type
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalByType(Long userId, BudgetType type) {
        BigDecimal total = budgetItemRepository.calculateTotalByUserAndType(userId, type);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Balance mensuelle actuelle
    @Transactional(readOnly = true)
    public BigDecimal calculateCurrentMonthBalance(Long userId) {
        LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        BigDecimal balance = budgetItemRepository.calculateMonthlyBalance(userId, startOfMonth, endOfMonth);
        return balance != null ? balance : BigDecimal.ZERO;
    }

    // Analyse financière complète
    @Transactional(readOnly = true)
    public FinancialSummary getFinancialSummary(Long userId) {
        BigDecimal totalIncome = calculateTotalByType(userId, BudgetType.INCOME);
        BigDecimal totalExpenses = calculateTotalByType(userId, BudgetType.EXPENSE);
        BigDecimal monthlyBalance = calculateCurrentMonthBalance(userId);

        // Top 5 des dépenses
        List<Object[]> topExpensesData = budgetItemRepository.findTopExpensesByUser(userId);
        Map<String, BigDecimal> topExpenses = topExpensesData.stream()
                .limit(5)
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (BigDecimal) row[1]
                ));

        return FinancialSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .monthlyBalance(monthlyBalance)
                .topExpenses(topExpenses)
                .build();
    }

    // Capacité d'emprunt estimée
    @Transactional(readOnly = true)
    public BigDecimal calculateBorrowingCapacity(Long userId) {
        BigDecimal monthlyBalance = calculateCurrentMonthBalance(userId);
        User user = userService.getUserById(userId);

        // Règle métier : 30% du revenu mental ou solde mensuel si positif
        BigDecimal incomeBasedCapacity = user.getMonthlyIncome().multiply(BigDecimal.valueOf(0.3));

        if (monthlyBalance.compareTo(BigDecimal.ZERO) > 0) {
            return monthlyBalance.multiply(BigDecimal.valueOf(3)); // 3 mois de solde positif
        }

        return incomeBasedCapacity;
    }

    // Capacité de prêt estimée
    @Transactional(readOnly = true)
    public BigDecimal calculateLendingCapacity(Long userId) {
        BigDecimal monthlyBalance = calculateCurrentMonthBalance(userId);

        // Règle métier : seulement si solde positif et > 500€
        if (monthlyBalance.compareTo(BigDecimal.valueOf(500)) > 0) {
            return monthlyBalance.multiply(BigDecimal.valueOf(2)); // 2 mois de solde positif
        }

        return BigDecimal.ZERO;
    }

    // Mise à jour d'un item
    public BudgetItem updateBudgetItem(Long itemId, String description, BigDecimal amount) {
        BudgetItem item = budgetItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item budgétaire non trouvé"));

        item.setDescription(description);
        item.setAmount(amount);

        return budgetItemRepository.save(item);
    }

    // Suppression d'un item
    public void deleteBudgetItem(Long itemId) {
        if (!budgetItemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Item budgétaire non trouvé");
        }

        budgetItemRepository.deleteById(itemId);
        log.info("Item budgétaire supprimé: ID={}", itemId);
    }

    // Classe interne pour le résumé financier
    @lombok.Data
    @lombok.Builder
    public static class FinancialSummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal monthlyBalance;
        private Map<String, BigDecimal> topExpenses;
    }
}
