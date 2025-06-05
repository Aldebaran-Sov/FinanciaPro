package com.example.financiapro.entity;

import lombok.Data;
import jakarta.persistence.*; // ← Manque probablement
import jakarta.validation.constraints.*; // ← Manque probablement
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String prenom;

    @NotBlank
    private String nom;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BudgetItem> budgetItems = new ArrayList<>();

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanRequest> emprunts = new ArrayList<>();

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanRequest> pretsAccordes = new ArrayList<>();

    @PrePersist
    public void initializeFields() {
        if (this.apiKey == null) {
            this.apiKey = UUID.randomUUID().toString();
        }
    }
}
