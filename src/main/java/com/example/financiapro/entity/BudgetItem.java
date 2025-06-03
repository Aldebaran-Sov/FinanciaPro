package com.example.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budget_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BudgetType type;

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void initializeFields() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }
}

enum BudgetType {
    INCOME("Revenus"),
    EXPENSE("Dépenses");

    private final String label;

    BudgetType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
