package com.example.financiapro.repository;

import com.example.financiapro.entity.LoanRequest;
import com.example.financiapro.entity.LoanStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    // Recherche par statut
    List<LoanRequest> findByStatut(LoanStatut statut);

    // Recherche par emprunteur
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.borrower.id = :borrowerId")
    List<LoanRequest> findByBorrowerId(@Param("borrowerId") Long borrowerId);

    // Recherche par prêteur
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.lender.id = :lenderId")
    List<LoanRequest> findByLenderId(@Param("lenderId") Long lenderId);

    // Demandes par emprunteur et statut
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.borrower.id = :borrowerId AND lr.statut = :statut")
    List<LoanRequest> findByBorrowerIdAndStatut(@Param("borrowerId") Long borrowerId, @Param("statut") LoanStatut statut);

    // Prêts par prêteur et statut
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.lender.id = :lenderId AND lr.statut = :statut")
    List<LoanRequest> findByLenderIdAndStatut(@Param("lenderId") Long lenderId, @Param("statut") LoanStatut statut);

    // Recherche par plage de montant
    @Query("SELECT lr FROM LoanRequest lr WHERE lr.montant BETWEEN :minMontant AND :maxMontant")
    List<LoanRequest> findByMontantBetween(@Param("minMontant") BigDecimal minMontant,
                                           @Param("maxMontant") BigDecimal maxMontant);

    // Total des montants demandés par un emprunteur
    @Query("SELECT COALESCE(SUM(lr.montant), 0) FROM LoanRequest lr WHERE lr.borrower.id = :borrowerId")
    BigDecimal getTotalAmountRequestedByBorrower(@Param("borrowerId") Long borrowerId);

    // Total des montants prêtés par un prêteur (seulement ACCEPTED)
    @Query("SELECT COALESCE(SUM(lr.montant), 0) FROM LoanRequest lr WHERE lr.lender.id = :lenderId AND lr.statut = com.example.financiapro.entity.LoanStatut.ACCEPTED")
    BigDecimal getTotalAmountLentByLender(@Param("lenderId") Long lenderId);
}
