package com.fp.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    @DecimalMin(value = "1.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Size(min = 10, max = 500)
    @Column(nullable = false)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LoanStatus status = LoanStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    @NotNull
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id")
    private User lender;

    @OneToMany(mappedBy = "loanRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Repayment> repayments = new ArrayList<>();

    // Constructeur personnalisé
    public LoanRequest(BigDecimal amount, String reason, User borrower) {
        this.amount = amount;
        this.reason = reason;
        this.borrower = borrower;
        this.status = LoanStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.repayments = new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Méthode utilitaire
    public boolean isAccepted() {
        return status == LoanStatus.ACCEPTED;
    }

    public boolean isPending() {
        return status == LoanStatus.PENDING;
    }
}

