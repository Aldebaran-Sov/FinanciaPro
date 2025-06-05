package com.example.financiapro.dto;


import com.example.financiapro.entity.BudgetType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItemDto implements Serializable {
    private Long id;
    private String description;
    private BigDecimal montant;
    private LocalDate date;
    private BudgetType type; // REVENUS ou DEPENSES

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_nom")
    private String userNom;

    @JsonProperty("user_prenom")
    private String userPrenom;
}
