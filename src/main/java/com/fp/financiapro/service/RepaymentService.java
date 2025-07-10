package com.fp.financiapro.service;

import com.fp.financiapro.entity.LoanRequest;
import com.fp.financiapro.entity.LoanStatus;
import com.fp.financiapro.entity.Repayment;
import com.fp.financiapro.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final LoanRequestService loanRequestService;

    // Création d'un remboursement
    public Repayment createRepayment(Long loanRequestId, BigDecimal amount) {
        log.info("Création d'un remboursement: prêt={}, montant={}", loanRequestId, amount);

        LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);

        // Validation : prêt doit être accepté
        if (loanRequest.getStatus() != LoanStatus.ACCEPTED) {
            throw new IllegalStateException("Le prêt doit être accepté pour être remboursé");
        }

        // Validation : montant à rembourser
        BigDecimal remainingAmount = calculateRemainingAmount(loanRequestId);
        if (amount.compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException("Montant supérieur au solde restant: " + remainingAmount);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du remboursement doit être positif");
        }

        Repayment repayment = Repayment.builder()
                .amount(amount)
                .loanRequest(loanRequest)
                .repaymentDate(LocalDateTime.now())
                .build();

        Repayment savedRepayment = repaymentRepository.save(repayment);

        // Vérification si le prêt est complètement remboursé
        BigDecimal newRemainingAmount = calculateRemainingAmount(loanRequestId);
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            loanRequest.setStatus(LoanStatus.COMPLETED);
            loanRequestService.updateLoanRequestStatus(loanRequestId, LoanStatus.COMPLETED);
            log.info("Prêt complètement remboursé: ID={}", loanRequestId);
        }

        log.info("Remboursement créé avec succès: ID={}", savedRepayment.getId());
        return savedRepayment;
    }

    // Remboursement complet
    public Repayment createFullRepayment(Long loanRequestId) {
        log.info("Remboursement complet du prêt: ID={}", loanRequestId);

        BigDecimal remainingAmount = calculateRemainingAmount(loanRequestId);

        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Le prêt est déjà remboursé");
        }

        return createRepayment(loanRequestId, remainingAmount);
    }

    // Calcul du montant restant
    @Transactional(readOnly = true)
    public BigDecimal calculateRemainingAmount(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);
        BigDecimal totalRepaid = calculateTotalRepaidAmount(loanRequestId);

        return loanRequest.getAmount().subtract(totalRepaid);
    }

    // Calcul du montant total remboursé
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRepaidAmount(Long loanRequestId) {
        BigDecimal total = repaymentRepository.calculateTotalRepaidAmount(loanRequestId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Récupération des remboursements par prêt
    @Transactional(readOnly = true)
    public List<Repayment> getRepaymentsByLoanRequest(Long loanRequestId) {
        return repaymentRepository.findByLoanRequestIdOrderByRepaymentDateDesc(loanRequestId);
    }

    // Statistiques de remboursement pour un emprunteur
    @Transactional(readOnly = true)
    public BigDecimal getTotalRepaidByUser(Long userId) {
        BigDecimal total = repaymentRepository.calculateTotalRepaidByUser(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Statistiques de remboursement reçu pour un prêteur
    @Transactional(readOnly = true)
    public BigDecimal getTotalReceivedByUser(Long userId) {
        BigDecimal total = repaymentRepository.calculateTotalReceivedByUser(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Récupération des remboursements par période
    @Transactional(readOnly = true)
    public List<Repayment> getRepaymentsByPeriod(LocalDateTime start, LocalDateTime end) {
        return repaymentRepository.findByRepaymentDateBetween(start, end);
    }

    // Analyse de remboursement
    @Transactional(readOnly = true)
    public RepaymentAnalysis getRepaymentAnalysis(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);
        BigDecimal totalRepaid = calculateTotalRepaidAmount(loanRequestId);
        BigDecimal remainingAmount = calculateRemainingAmount(loanRequestId);
        List<Repayment> repayments = getRepaymentsByLoanRequest(loanRequestId);

        BigDecimal repaymentPercentage = BigDecimal.ZERO;
        if (loanRequest.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            repaymentPercentage = totalRepaid
                    .multiply(BigDecimal.valueOf(100))
                    .divide(loanRequest.getAmount(), 2, java.math.RoundingMode.HALF_UP);
        }

        return RepaymentAnalysis.builder()
                .loanAmount(loanRequest.getAmount())
                .totalRepaid(totalRepaid)
                .remainingAmount(remainingAmount)
                .repaymentPercentage(repaymentPercentage)
                .repaymentCount(repayments.size())
                .isCompleted(remainingAmount.compareTo(BigDecimal.ZERO) == 0)
                .build();
    }

    // Validation des droits (emprunteur uniquement)
    @Transactional(readOnly = true)
    public boolean canUserRepayLoan(Long userId, Long loanRequestId) {
        LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);
        return loanRequest.getBorrower().getId().equals(userId);
    }

    // Classe interne pour l'analyse de remboursement
    @lombok.Data
    @lombok.Builder
    public static class RepaymentAnalysis {
        private BigDecimal loanAmount;
        private BigDecimal totalRepaid;
        private BigDecimal remainingAmount;
        private BigDecimal repaymentPercentage;
        private int repaymentCount;
        private boolean isCompleted;
    }
}
