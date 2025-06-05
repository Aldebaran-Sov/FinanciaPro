package com.example.financiapro.controller;

import com.example.financiapro.entity.Repayment;
import com.example.financiapro.service.RepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {

    @Autowired
    private RepaymentService repaymentService;

    // ==================== CRUD DE BASE ====================

    @PostMapping
    public ResponseEntity<Repayment> createRepayment(@RequestBody Repayment repayment) {
        try {
            Repayment savedRepayment = repaymentService.createRepayment(repayment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRepayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repayment> getRepaymentById(@PathVariable Long id) {
        Optional<Repayment> repayment = repaymentService.getRepaymentById(id);
        return repayment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Repayment>> getAllRepayments() {
        List<Repayment> repayments = repaymentService.getAllRepayments();
        return ResponseEntity.ok(repayments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repayment> updateRepayment(@PathVariable Long id, @RequestBody Repayment repayment) {
        try {
            Repayment updatedRepayment = repaymentService.updateRepayment(id, repayment);
            return ResponseEntity.ok(updatedRepayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepayment(@PathVariable Long id) {
        try {
            repaymentService.deleteRepayment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== RECHERCHE PAR PRÊT ====================

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<Repayment>> getRepaymentsByLoan(@PathVariable Long loanId) {
        List<Repayment> repayments = repaymentService.getRepaymentsByLoan(loanId);
        return ResponseEntity.ok(repayments);
    }

    // ==================== RECHERCHE PAR PÉRIODE ====================

    @GetMapping("/period")
    public ResponseEntity<List<Repayment>> getRepaymentsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Repayment> repayments = repaymentService.getRepaymentsByPeriod(startDate, endDate);
        return ResponseEntity.ok(repayments);
    }

    // ==================== STATISTIQUES ====================

    @GetMapping("/loan/{loanId}/total")
    public ResponseEntity<BigDecimal> getTotalRepaidAmount(@PathVariable Long loanId) {
        BigDecimal total = repaymentService.getTotalRepaidAmount(loanId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/loan/{loanId}/count")
    public ResponseEntity<Integer> getRepaymentCount(@PathVariable Long loanId) {
        int count = repaymentService.getRepaymentCount(loanId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/loan/{loanId}/average")
    public ResponseEntity<BigDecimal> getAverageRepaymentAmount(@PathVariable Long loanId) {
        BigDecimal average = repaymentService.getAverageRepaymentAmount(loanId);
        return ResponseEntity.ok(average);
    }

    // ==================== REMBOURSEMENT PLANIFIÉ ====================

    @PostMapping("/scheduled")
    public ResponseEntity<Repayment> createScheduledRepayment(
            @RequestParam Long loanId,
            @RequestParam BigDecimal amount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate scheduledDate,
            @RequestParam(required = false) String comment) {
        try {
            Repayment repayment = repaymentService.createScheduledRepayment(loanId, amount, scheduledDate, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(repayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
