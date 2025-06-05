package com.example.financiapro.dto;

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
public class RepaymentDto implements Serializable {
    private Long id;
    private BigDecimal montant;
    private LocalDate date;
    private String commentaire;

    @JsonProperty("loan_request_id")
    private Long loanRequestId;

    // Infos du prêt associé
    @JsonProperty("loan_montant_total")
    private BigDecimal loanMontantTotal;

    @JsonProperty("borrower_nom")
    private String borrowerNom;

    @JsonProperty("lender_nom")
    private String lenderNom;
}
