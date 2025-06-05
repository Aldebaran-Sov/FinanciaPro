package com.example.financiapro.dto;

import com.example.financiapro.entity.LoanStatut;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanRequestDto {

    private Long id;

    @NotNull(message = "L'ID de l'emprunteur est obligatoire")
    private Long borrowerId;

    private String borrowerNom;

    private String borrowerPrenom;

    private Long lenderId;

    private String lenderNom;

    private String lenderPrenom;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;

    @NotNull(message = "Le taux d'intérêt est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux d'intérêt doit être >= 0")
    @DecimalMax(value = "100.0", message = "Le taux d'intérêt doit être <= 100")
    private BigDecimal tauxInteret;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée minimum est de 1 mois")
    @Max(value = 360, message = "La durée maximum est de 360 mois")
    private Integer dureeEnMois;

    private LoanStatut statut;

    private LocalDate dateCreation;

    private LocalDate dateAcceptation;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaire;
}
