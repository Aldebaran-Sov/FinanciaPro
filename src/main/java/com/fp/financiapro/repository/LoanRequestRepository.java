package com.fp.financiapro.repository;

import com.fp.financiapro.entity.LoanRequest;
import com.fp.financiapro.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    // Requêtes par utilisateur
    List<LoanRequest> findByBorrowerIdOrderByCreatedAtDesc(Long borrowerId);
    List<LoanRequest> findByLenderIdOrderByCreatedAtDesc(Long lenderId);

    // Requêtes par statut
    List<LoanRequest> findByStatus(LoanStatus status);
    List<LoanRequest> findByStatusOrderByCreatedAtDesc(LoanStatus status);

    // Demandes disponibles pour un prêteur potentiel
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.status = 'PENDING' AND lr.borrower.id != :userId")
    List<LoanRequest> findAvailableLoanRequests(@Param("userId") Long userId);

    // Demandes par montant
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.status = 'PENDING' AND lr.amount <= :maxAmount")
    List<LoanRequest> findPendingLoansByMaxAmount(@Param("maxAmount") BigDecimal maxAmount);

    // Statistiques pour un utilisateur
    @Query("SELECT COUNT(lr) FROM LoanRequest lr WHERE lr.borrower.id = :userId AND lr.status = :status")
    long countByBorrowerIdAndStatus(@Param("userId") Long userId, @Param("status") LoanStatus status);

    @Query("SELECT SUM(lr.amount) FROM LoanRequest lr WHERE lr.borrower.id = :userId AND lr.status = 'ACCEPTED'")
    BigDecimal calculateTotalBorrowedAmount(@Param("userId") Long userId);

    @Query("SELECT SUM(lr.amount) FROM LoanRequest lr WHERE lr.lender.id = :userId AND lr.status = 'ACCEPTED'")
    BigDecimal calculateTotalLentAmount(@Param("userId") Long userId);

    // Requêtes temporelles
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.createdAt BETWEEN :start AND :end")
    List<LoanRequest> findByCreatedAtBetween(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    // Matching intelligent : demandes compatibles avec capacité financière
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.status = 'PENDING' " +
            "AND lr.amount <= :maxLendingCapacity " +
            "AND lr.borrower.id != :lenderId " +
            "ORDER BY lr.createdAt ASC")
    List<LoanRequest> findMatchingLoanRequests(@Param("lenderId") Long lenderId,
                                               @Param("maxLendingCapacity") BigDecimal maxLendingCapacity);
}
