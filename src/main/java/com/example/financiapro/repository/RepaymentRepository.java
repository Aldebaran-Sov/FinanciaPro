package com.example.financiapro.repository;

import com.example.financiapro.entity.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

    // Remboursements par prêt
    List<Repayment> findByLoanRequestIdOrderByDateDesc(Long loanRequestId);

    // Remboursements par période
    List<Repayment> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Stats remboursements
    @Query("SELECT SUM(r.montant) FROM Repayment r WHERE r.loanRequest.id = :loanRequestId")
    Optional<BigDecimal> sumMontantByLoanRequestId(@Param("loanRequestId") Long loanRequestId);

    // Dernier remboursement d'un prêt
    @Query("SELECT r FROM Repayment r WHERE r.loanRequest.id = :loanRequestId ORDER BY r.date DESC")
    Optional<Repayment> findLastRepaymentByLoanRequestId(@Param("loanRequestId") Long loanRequestId, Pageable pageable);
}

