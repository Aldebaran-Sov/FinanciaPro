package com.example.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "loan_requests")
@Data
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id")
    private User lender;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @NotNull(message = "Le taux d'intérêt est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux d'intérêt doit être >= 0")
    @DecimalMax(value = "100.0", message = "Le taux d'intérêt doit être <= 100")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal tauxInteret;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée minimum est de 1 mois")
    @Max(value = 360, message = "La durée maximum est de 360 mois")
    @Column(nullable = false)
    private Integer dureeEnMois;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private LoanStatut statut;

    @Column(nullable = false)
    private LocalDate dateCreation;

    private LocalDate dateAcceptation;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String commentaire;

    @OneToMany(mappedBy = "loanRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Repayment> repayments = new ArrayList<>();

    @PrePersist
    public void initializeFields() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDate.now();
        }
        if (this.statut == null) {
            this.statut = LoanStatut.PENDING;
        }
    }
}


