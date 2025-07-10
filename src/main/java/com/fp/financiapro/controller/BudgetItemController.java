package com.fp.financiapro.controller;

import com.fp.financiapro.entity.BudgetItem;
import com.fp.financiapro.entity.BudgetType;
import com.fp.financiapro.service.BudgetItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/budget-items")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BudgetItemController {

    private final BudgetItemService budgetItemService;

    // Création d'un élément budgétaire
    @PostMapping
    public ResponseEntity<BudgetItem> createBudgetItem(@RequestBody CreateBudgetItemRequest request) {
        log.info("Création d'un élément budgétaire: utilisateur={}, type={}, montant={}",
                request.getUserId(), request.getType(), request.getAmount());

        try {
            BudgetItem budgetItem = budgetItemService.createBudgetItem(
                    request.getUserId(),
                    request.getDescription(),
                    request.getAmount(),
                    request.getType()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(budgetItem);
        } catch (IllegalArgumentException e) {
            log.error("Erreur création élément budgétaire: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Récupération des éléments budgétaires par utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetItem>> getBudgetItemsByUser(@PathVariable Long userId) {
        List<BudgetItem> budgetItems = budgetItemService.getBudgetItemsByUser(userId);
        return ResponseEntity.ok(budgetItems);
    }

    // Récupération par utilisateur et type
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<BudgetItem>> getBudgetItemsByUserAndType(@PathVariable Long userId,
                                                                        @PathVariable BudgetType type) {
        List<BudgetItem> budgetItems = budgetItemService.getBudgetItemsByUserAndType(userId, type);
        return ResponseEntity.ok(budgetItems);
    }

    // Calcul du total par type
    @GetMapping("/total/user/{userId}/type/{type}")
    public ResponseEntity<BigDecimal> calculateTotalByType(@PathVariable Long userId,
                                                           @PathVariable BudgetType type) {
        BigDecimal total = budgetItemService.calculateTotalByType(userId, type);
        return ResponseEntity.ok(total);
    }

    // Balance mensuelle actuelle
    @GetMapping("/balance/user/{userId}/current-month")
    public ResponseEntity<BigDecimal> calculateCurrentMonthBalance(@PathVariable Long userId) {
        BigDecimal balance = budgetItemService.calculateCurrentMonthBalance(userId);
        return ResponseEntity.ok(balance);
    }

    // Résumé financier complet
    @GetMapping("/financial-summary/user/{userId}")
    public ResponseEntity<BudgetItemService.FinancialSummary> getFinancialSummary(@PathVariable Long userId) {
        BudgetItemService.FinancialSummary summary = budgetItemService.getFinancialSummary(userId);
        return ResponseEntity.ok(summary);
    }

    // Capacité d'emprunt estimée
    @GetMapping("/borrowing-capacity/user/{userId}")
    public ResponseEntity<BigDecimal> calculateBorrowingCapacity(@PathVariable Long userId) {
        BigDecimal capacity = budgetItemService.calculateBorrowingCapacity(userId);
        return ResponseEntity.ok(capacity);
    }

    // Capacité de prêt estimée
    @GetMapping("/lending-capacity/user/{userId}")
    public ResponseEntity<BigDecimal> calculateLendingCapacity(@PathVariable Long userId) {
        BigDecimal capacity = budgetItemService.calculateLendingCapacity(userId);
        return ResponseEntity.ok(capacity);
    }

    // Mise à jour d'un élément budgétaire
    @PutMapping("/{itemId}")
    public ResponseEntity<BudgetItem> updateBudgetItem(@PathVariable Long itemId,
                                                       @RequestBody UpdateBudgetItemRequest request) {
        try {
            BudgetItem updatedItem = budgetItemService.updateBudgetItem(
                    itemId,
                    request.getDescription(),
                    request.getAmount()
            );

            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            log.error("Erreur mise à jour élément budgétaire: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Suppression d'un élément budgétaire
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteBudgetItem(@PathVariable Long itemId) {
        try {
            budgetItemService.deleteBudgetItem(itemId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erreur suppression élément budgétaire: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // DTOs pour les requêtes
    @lombok.Data
    public static class CreateBudgetItemRequest {
        private Long userId;
        private String description;
        private BigDecimal amount;
        private BudgetType type;
    }

    @lombok.Data
    public static class UpdateBudgetItemRequest {
        private String description;
        private BigDecimal amount;
    }
}
