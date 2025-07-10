package com.fp.financiapro.repository;

import com.fp.financiapro.entity.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

    // Remboursements par prêt
    List<Repayment> findByLoanRequestIdOrderByRepaymentDateDesc(Long loanRequestId);

    // Calculs de remboursement
    @Query("SELECT SUM(r.amount) FROM Repayment r WHERE r.loanRequest.id = :loanRequestId")
    BigDecimal calculateTotalRepaidAmount(@Param("loanRequestId") Long loanRequestId);

    // Montant restant à rembourser
    @Query("SELECT (lr.amount - COALESCE(SUM(r.amount), 0)) " +
            "FROM LoanRequest lr LEFT JOIN lr.repayments r " +
            "WHERE lr.id = :loanRequestId " +
            "GROUP BY lr.id, lr.amount")
    BigDecimal calculateRemainingAmount(@Param("loanRequestId") Long loanRequestId);

    // Remboursements par période
    @Query("SELECT r FROM Repayment r WHERE r.repaymentDate BETWEEN :start AND :end")
    List<Repayment> findByRepaymentDateBetween(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    // Statistiques pour un utilisateur (en tant qu'emprunteur)
    @Query("SELECT SUM(r.amount) FROM Repayment r WHERE r.loanRequest.borrower.id = :userId")
    BigDecimal calculateTotalRepaidByUser(@Param("userId") Long userId);

    // Statistiques pour un utilisateur (en tant que prêteur)
    @Query("SELECT SUM(r.amount) FROM Repayment r WHERE r.loanRequest.lender.id = :userId")
    BigDecimal calculateTotalReceivedByUser(@Param("userId") Long userId);
}
