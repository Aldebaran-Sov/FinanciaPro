package com.fp.financiapro.service;

import com.fp.financiapro.entity.LoanRequest;
import com.fp.financiapro.entity.LoanStatus;
import com.fp.financiapro.entity.User;
import com.fp.financiapro.repository.LoanRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserService userService;
    private final BudgetItemService budgetItemService;

    // Création d'une demande de prêt
    public LoanRequest createLoanRequest(Long borrowerId, BigDecimal amount, String reason) {
        log.info("Création d'une demande de prêt: utilisateur={}, montant={}", borrowerId, amount);

        User borrower = userService.getUserById(borrowerId);

        // Validation métier : capacité d'emprunt
        BigDecimal borrowingCapacity = budgetItemService.calculateBorrowingCapacity(borrowerId);
        if (amount.compareTo(borrowingCapacity) > 0) {
            throw new IllegalArgumentException("Montant demandé supérieur à la capacité d'emprunt estimée: " + borrowingCapacity);
        }

        LoanRequest loanRequest = LoanRequest.builder()
                .amount(amount)
                .reason(reason)
                .borrower(borrower)
                .status(LoanStatus.PENDING)
                .build();

        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        log.info("Demande de prêt créée avec succès: ID={}", savedLoanRequest.getId());

        return savedLoanRequest;
    }

    // Acceptation d'une demande de prêt
    public LoanRequest acceptLoanRequest(Long loanRequestId, Long lenderId) {
        log.info("Acceptation d'une demande de prêt: ID={}, prêteur={}", loanRequestId, lenderId);

        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt non trouvée"));

        if (loanRequest.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("La demande de prêt n'est plus en attente");
        }

        User lender = userService.getUserById(lenderId);

        // Validation métier : ne peut pas se prêter à soi-même
        if (loanRequest.getBorrower().getId().equals(lenderId)) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas se prêter à lui-même");
        }

        // Validation capacité de prêt
        BigDecimal lendingCapacity = budgetItemService.calculateLendingCapacity(lenderId);
        if (loanRequest.getAmount().compareTo(lendingCapacity) > 0) {
            throw new IllegalArgumentException("Montant supérieur à votre capacité de prêt: " + lendingCapacity);
        }

        loanRequest.setLender(lender);
        loanRequest.setStatus(LoanStatus.ACCEPTED);

        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        log.info("Demande de prêt acceptée avec succès: ID={}", savedLoanRequest.getId());

        return savedLoanRequest;
    }

    // Rejet d'une demande de prêt
    public LoanRequest refuseLoanRequest(Long loanRequestId, Long lenderId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt non trouvée"));

        if (loanRequest.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("La demande de prêt n'est plus en attente");
        }

        loanRequest.setStatus(LoanStatus.REFUSED);
        return loanRequestRepository.save(loanRequest);
    }

    // Récupération des demandes disponibles
    @Transactional(readOnly = true)
    public List<LoanRequest> getAvailableLoanRequests(Long userId) {
        return loanRequestRepository.findAvailableLoanRequests(userId);
    }

    // Matching intelligent
    @Transactional(readOnly = true)
    public List<LoanRequest> findMatchingLoanRequests(Long lenderId) {
        BigDecimal lendingCapacity = budgetItemService.calculateLendingCapacity(lenderId);
        return loanRequestRepository.findMatchingLoanRequests(lenderId, lendingCapacity);
    }

    // Récupération des demandes par utilisateur
    @Transactional(readOnly = true)
    public List<LoanRequest> getLoanRequestsByBorrower(Long borrowerId) {
        return loanRequestRepository.findByBorrowerIdOrderByCreatedAtDesc(borrowerId);
    }

    @Transactional(readOnly = true)
    public List<LoanRequest> getLoanRequestsByLender(Long lenderId) {
        return loanRequestRepository.findByLenderIdOrderByCreatedAtDesc(lenderId);
    }

    // Statistiques
    @Transactional(readOnly = true)
    public BigDecimal getTotalBorrowedAmount(Long userId) {
        BigDecimal total = loanRequestRepository.calculateTotalBorrowedAmount(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalLentAmount(Long userId) {
        BigDecimal total = loanRequestRepository.calculateTotalLentAmount(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Récupération sécurisée
    @Transactional(readOnly = true)
    public LoanRequest getLoanRequestById(Long loanRequestId) {
        return loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt non trouvée: " + loanRequestId));
    }

    public LoanRequest updateLoanRequestStatus(Long loanRequestId, LoanStatus status) {
        LoanRequest loanRequest = getLoanRequestById(loanRequestId);
        loanRequest.setStatus(status);
        return loanRequestRepository.save(loanRequest);
    }

}
