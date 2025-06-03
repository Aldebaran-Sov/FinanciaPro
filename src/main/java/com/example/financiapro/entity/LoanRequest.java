package com.example.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "loan_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Positive
    @Min(0)
    @Max(100)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal tauxInteret;

    @Positive
    @Min(1)
    @Max(360)
    @Column(nullable = false)
    private Integer dureeEnMois;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private LoanStatut statut;

    @Column(nullable = false)
    private LocalDate dateCreation;

    private LocalDate dateAcceptation;

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

enum LoanStatut {
    PENDING("En attente"),
    ACCEPTED("Accepter"),
    REFUSED("Refuser");

    private final String label;

    LoanStatut(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
