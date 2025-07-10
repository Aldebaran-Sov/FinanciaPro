package com.fp.financiapro.controller;

import com.fp.financiapro.entity.Repayment;
import com.fp.financiapro.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/repayments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RepaymentController {

    private final RepaymentService repaymentService;

    // Création d'un remboursement
    @PostMapping
    public ResponseEntity<Repayment> createRepayment(@RequestBody CreateRepaymentRequest request) {
        log.info("Création d'un remboursement: prêt={}, montant={}",
                request.getLoanRequestId(), request.getAmount());

        try {
            Repayment repayment = repaymentService.createRepayment(
                    request.getLoanRequestId(),
                    request.getAmount()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(repayment);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Erreur création remboursement: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Remboursement complet
    @PostMapping("/full")
    public ResponseEntity<Repayment> createFullRepayment(@RequestBody FullRepaymentRequest request) {
        log.info("Remboursement complet du prêt: {}", request.getLoanRequestId());

        try {
            Repayment repayment = repaymentService.createFullRepayment(request.getLoanRequestId());
            return ResponseEntity.status(HttpStatus.CREATED).body(repayment);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Erreur remboursement complet: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Récupération des remboursements par prêt
    @GetMapping("/loan/{loanRequestId}")
    public ResponseEntity<List<Repayment>> getRepaymentsByLoanRequest(@PathVariable Long loanRequestId) {
        List<Repayment> repayments = repaymentService.getRepaymentsByLoanRequest(loanRequestId);
        return ResponseEntity.ok(repayments);
    }

    // Calcul du montant total remboursé
    @GetMapping("/total-repaid/{loanRequestId}")
    public ResponseEntity<BigDecimal> getTotalRepaidAmount(@PathVariable Long loanRequestId) {
        BigDecimal totalRepaid = repaymentService.calculateTotalRepaidAmount(loanRequestId);
        return ResponseEntity.ok(totalRepaid);
    }

    // Calcul du montant restant
    @GetMapping("/remaining/{loanRequestId}")
    public ResponseEntity<BigDecimal> getRemainingAmount(@PathVariable Long loanRequestId) {
        BigDecimal remainingAmount = repaymentService.calculateRemainingAmount(loanRequestId);
        return ResponseEntity.ok(remainingAmount);
    }

    // Analyse de remboursement
    @GetMapping("/analysis/{loanRequestId}")
    public ResponseEntity<RepaymentService.RepaymentAnalysis> getRepaymentAnalysis(@PathVariable Long loanRequestId) {
        try {
            RepaymentService.RepaymentAnalysis analysis = repaymentService.getRepaymentAnalysis(loanRequestId);
            return ResponseEntity.ok(analysis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Statistiques de remboursement pour un utilisateur
    @GetMapping("/stats/repaid-by-user/{userId}")
    public ResponseEntity<BigDecimal> getTotalRepaidByUser(@PathVariable Long userId) {
        BigDecimal totalRepaid = repaymentService.getTotalRepaidByUser(userId);
        return ResponseEntity.ok(totalRepaid);
    }

    @GetMapping("/stats/received-by-user/{userId}")
    public ResponseEntity<BigDecimal> getTotalReceivedByUser(@PathVariable Long userId) {
        BigDecimal totalReceived = repaymentService.getTotalReceivedByUser(userId);
        return ResponseEntity.ok(totalReceived);
    }

    // Remboursements par période
    @GetMapping("/period")
    public ResponseEntity<List<Repayment>> getRepaymentsByPeriod(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<Repayment> repayments = repaymentService.getRepaymentsByPeriod(startDate, endDate);
        return ResponseEntity.ok(repayments);
    }

    // Validation des droits
    @GetMapping("/can-repay/{userId}/{loanRequestId}")
    public ResponseEntity<Boolean> canUserRepayLoan(@PathVariable Long userId,
                                                    @PathVariable Long loanRequestId) {
        boolean canRepay = repaymentService.canUserRepayLoan(userId, loanRequestId);
        return ResponseEntity.ok(canRepay);
    }

    // DTOs pour les requêtes
    @lombok.Data
    public static class CreateRepaymentRequest {
        private Long loanRequestId;
        private BigDecimal amount;
    }

    @lombok.Data
    public static class FullRepaymentRequest {
        private Long loanRequestId;
    }
}
