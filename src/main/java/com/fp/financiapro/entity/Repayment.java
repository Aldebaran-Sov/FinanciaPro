package com.fp.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime repaymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_request_id", nullable = false)
    @NotNull
    private LoanRequest loanRequest;

    // Constructeur personnalisé
    public Repayment(BigDecimal amount, LoanRequest loanRequest) {
        this.amount = amount;
        this.loanRequest = loanRequest;
        this.repaymentDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        repaymentDate = LocalDateTime.now();
    }
}
