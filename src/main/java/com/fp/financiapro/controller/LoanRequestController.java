package com.fp.financiapro.controller;

import com.fp.financiapro.entity.LoanRequest;
import com.fp.financiapro.service.LoanRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loan-requests")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    // Création d'une demande de prêt
    @PostMapping
    public ResponseEntity<LoanRequest> createLoanRequest(@RequestBody CreateLoanRequestRequest request) {
        log.info("Création d'une demande de prêt: utilisateur={}, montant={}",
                request.getBorrowerId(), request.getAmount());

        try {
            LoanRequest loanRequest = loanRequestService.createLoanRequest(
                    request.getBorrowerId(),
                    request.getAmount(),
                    request.getDescription()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(loanRequest);
        } catch (IllegalArgumentException e) {
            log.error("Erreur création demande de prêt: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Récupération d'une demande par ID
    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getLoanRequestById(@PathVariable Long id) {
        try {
            LoanRequest loanRequest = loanRequestService.getLoanRequestById(id);
            return ResponseEntity.ok(loanRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Acceptation d'une demande de prêt
    @PostMapping("/{id}/accept")
    public ResponseEntity<LoanRequest> acceptLoanRequest(@PathVariable Long id,
                                                         @RequestBody AcceptLoanRequestRequest request) {
        try {
            LoanRequest acceptedLoan = loanRequestService.acceptLoanRequest(id, request.getLenderId());
            return ResponseEntity.ok(acceptedLoan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Erreur acceptation prêt: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Refus d'une demande de prêt
    @PostMapping("/{id}/refuse")
    public ResponseEntity<LoanRequest> refuseLoanRequest(@PathVariable Long id,
                                                         @RequestBody RefuseLoanRequestRequest request) {
        try {
            LoanRequest refusedLoan = loanRequestService.refuseLoanRequest(id, request.getLenderId());
            return ResponseEntity.ok(refusedLoan);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Erreur refus prêt: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Demandes disponibles pour un prêteur
    @GetMapping("/available/{lenderId}")
    public ResponseEntity<List<LoanRequest>> getAvailableLoanRequests(@PathVariable Long lenderId) {
        List<LoanRequest> availableRequests = loanRequestService.getAvailableLoanRequests(lenderId);
        return ResponseEntity.ok(availableRequests);
    }

    // Demandes avec matching intelligent
    @GetMapping("/matching/{lenderId}")
    public ResponseEntity<List<LoanRequest>> getMatchingLoanRequests(@PathVariable Long lenderId) {
        List<LoanRequest> matchingRequests = loanRequestService.findMatchingLoanRequests(lenderId);
        return ResponseEntity.ok(matchingRequests);
    }

    // Demandes d'un emprunteur
    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<LoanRequest>> getLoanRequestsByBorrower(@PathVariable Long borrowerId) {
        List<LoanRequest> requests = loanRequestService.getLoanRequestsByBorrower(borrowerId);
        return ResponseEntity.ok(requests);
    }

    // Demandes acceptées par un prêteur
    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<List<LoanRequest>> getLoanRequestsByLender(@PathVariable Long lenderId) {
        List<LoanRequest> requests = loanRequestService.getLoanRequestsByLender(lenderId);
        return ResponseEntity.ok(requests);
    }

    // Statistiques de prêt
    @GetMapping("/stats/borrowed/{borrowerId}")
    public ResponseEntity<BigDecimal> getTotalBorrowedAmount(@PathVariable Long borrowerId) {
        BigDecimal totalBorrowed = loanRequestService.getTotalBorrowedAmount(borrowerId);
        return ResponseEntity.ok(totalBorrowed);
    }

    @GetMapping("/stats/lent/{lenderId}")
    public ResponseEntity<BigDecimal> getTotalLentAmount(@PathVariable Long lenderId) {
        BigDecimal totalLent = loanRequestService.getTotalLentAmount(lenderId);
        return ResponseEntity.ok(totalLent);
    }

    // DTOs pour les requêtes
    @lombok.Data
    public static class CreateLoanRequestRequest {
        private Long borrowerId;
        private BigDecimal amount;
        private String description;
    }

    @lombok.Data
    public static class AcceptLoanRequestRequest {
        private Long lenderId;
    }

    @lombok.Data
    public static class RefuseLoanRequestRequest {
        private Long lenderId;
    }
}
