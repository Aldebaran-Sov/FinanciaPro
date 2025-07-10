package com.fp.financiapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BudgetItem> budgetItems = new ArrayList<>();

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LoanRequest> borrowedLoans = new ArrayList<>();

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LoanRequest> lentLoans = new ArrayList<>();

    // Constructeur personnalisé pour la création
    public User(String firstName, String lastName, String email, BigDecimal monthlyIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.monthlyIncome = monthlyIncome;
        this.createdAt = LocalDateTime.now();
        this.budgetItems = new ArrayList<>();
        this.borrowedLoans = new ArrayList<>();
        this.lentLoans = new ArrayList<>();
    }

    // Méthode utilitaire
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
