package com.example.financiapro.service;

import com.example.financiapro.entity.LoanRequest;
import com.example.financiapro.entity.LoanStatut;
import com.example.financiapro.entity.Repayment;
import com.example.financiapro.repository.RepaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final LoanRequestService loanService; // Injection circulaire résolue par @Lazy si nécessaire

    @Autowired
    public RepaymentService(RepaymentRepository repaymentRepository) {
        this.repaymentRepository = repaymentRepository;
        // LoanService sera injecté plus tard pour éviter la dépendance circulaire
        this.loanService = null;
    }

    // Setter pour résoudre la dépendance circulaire
    public void setLoanService(LoanRequestService loanService) {
        // Cette approche peut être utilisée ou bien utiliser @Lazy
    }

    // ==================== CRUD DE BASE ====================

    public Repayment createRepayment(Repayment repayment) {
        validateRepayment(repayment);

        if (repayment.getDate() == null) {
            repayment.setDate(LocalDate.now());
        }

        Repayment savedRepayment = repaymentRepository.save(repayment);

        // Vérifier si le prêt est entièrement remboursé
        checkAndUpdateLoanStatus(repayment.getLoanRequest().getId());

        return savedRepayment;
    }

    @Transactional(readOnly = true)
    public Optional<Repayment> getRepaymentById(Long id) {
        return repaymentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Repayment> getAllRepayments() {
        return repaymentRepository.findAll();
    }

    public Repayment updateRepayment(Long id, Repayment updatedRepayment) {
        return repaymentRepository.findById(id)
                .map(existing -> {
                    existing.setMontant(updatedRepayment.getMontant());
                    existing.setDate(updatedRepayment.getDate());
                    existing.setCommentaire(updatedRepayment.getCommentaire());

                    Repayment saved = repaymentRepository.save(existing);

                    // Re-vérifier le statut du prêt
                    checkAndUpdateLoanStatus(existing.getLoanRequest().getId());

                    return saved;
                })
                .orElseThrow(() -> new RuntimeException("Remboursement non trouvé avec l'ID : " + id));
    }

    public void deleteRepayment(Long id) {
        Repayment repayment = repaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remboursement non trouvé avec l'ID : " + id));

        Long loanId = repayment.getLoanRequest().getId();

        repaymentRepository.deleteById(id);

        // Re-vérifier le statut du prêt après suppression
        checkAndUpdateLoanStatus(loanId);
    }

    // ==================== RECHERCHE PAR PRÊT ====================

    @Transactional(readOnly = true)
    public List<Repayment> getRepaymentsByLoan(Long loanRequestId) {
        return repaymentRepository.findByLoanRequestIdOrderByDateDesc(loanRequestId);
    }

    // ==================== RECHERCHE PAR PÉRIODE ====================

    @Transactional(readOnly = true)
    public List<Repayment> getRepaymentsByPeriod(LocalDate startDate, LocalDate endDate) {
        return repaymentRepository.findByDateBetween(startDate, endDate);
    }

    // ==================== CALCULS & STATISTIQUES ====================

    @Transactional(readOnly = true)
    public BigDecimal getTotalRepaidAmount(Long loanRequestId) {
        return repaymentRepository.sumMontantByLoanRequestId(loanRequestId)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal getRemainingAmount(Long loanRequestId, BigDecimal totalLoanAmount) {
        BigDecimal totalRepaid = getTotalRepaidAmount(loanRequestId);
        return totalLoanAmount.subtract(totalRepaid);
    }

    @Transactional(readOnly = true)
    public boolean isLoanFullyRepaid(Long loanRequestId, BigDecimal totalLoanAmount) {
        return getRemainingAmount(loanRequestId, totalLoanAmount).compareTo(BigDecimal.ZERO) <= 0;
    }

    @Transactional(readOnly = true)
    public BigDecimal getRepaymentPercentage(Long loanRequestId, BigDecimal totalLoanAmount) {
        if (totalLoanAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalRepaid = getTotalRepaidAmount(loanRequestId);
        return totalRepaid.multiply(new BigDecimal("100"))
                .divide(totalLoanAmount, 2, BigDecimal.ROUND_HALF_UP);
    }

    // ==================== GESTION AUTOMATIQUE DU STATUT ====================

    private void checkAndUpdateLoanStatus(Long loanRequestId) {
        // Note: Pour éviter la dépendance circulaire, tu pourrais :
        // 1. Injecter LoanRequestRepository directement ici
        // 2. Utiliser @Lazy sur LoanService
        // 3. Créer un service intermédiaire

        // Pour l'instant, logique basique sans l'injection de LoanService
        // Tu peux adapter selon ta préférence architecturale
    }

    // ==================== REMBOURSEMENTS PLANIFIÉS ====================

    public Repayment createScheduledRepayment(Long loanRequestId, BigDecimal amount, LocalDate scheduledDate, String comment) {
        // Vérifier que le prêt existe et est actif
        // (ici tu pourrais injecter LoanRequestRepository directement)

        Repayment repayment = new Repayment();
        // repayment.setLoanRequest(...); // Set depuis le repository
        repayment.setMontant(amount);
        repayment.setDate(scheduledDate);
        repayment.setCommentaire(comment);

        return createRepayment(repayment);
    }

    // ==================== VALIDATION ====================

    private void validateRepayment(Repayment repayment) {
        if (repayment.getLoanRequest() == null) {
            throw new RuntimeException("Le prêt associé est obligatoire");
        }

        if (repayment.getMontant() == null || repayment.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant du remboursement doit être positif");
        }

        if (repayment.getDate() != null && repayment.getDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("La date de remboursement ne peut pas être dans le futur");
        }

        // Vérifier que le prêt est dans un état qui permet les remboursements
        LoanRequest loan = repayment.getLoanRequest();
        if (loan.getStatut() != LoanStatut.ACCEPTED) {
            throw new RuntimeException("Impossible de rembourser un prêt qui n'est pas accepté");
        }
    }

    // ==================== UTILITAIRES ====================

    @Transactional(readOnly = true)
    public int getRepaymentCount(Long loanRequestId) {
        return getRepaymentsByLoan(loanRequestId).size();
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageRepaymentAmount(Long loanRequestId) {
        List<Repayment> repayments = getRepaymentsByLoan(loanRequestId);

        if (repayments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = repayments.stream()
                .map(Repayment::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(new BigDecimal(repayments.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
}
