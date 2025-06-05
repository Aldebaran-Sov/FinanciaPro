package com.example.financiapro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String nom;
    private String prenom;
    private String email;

    @JsonProperty("api_key")
    private String apiKey;

    // Métadonnées utiles
    @JsonProperty("nombre_budget_items")
    private Integer nombreBudgetItems;

    @JsonProperty("nombre_prets_empruntes")
    private Integer nombrePretsEmpruntes;

    @JsonProperty("nombre_prets_pretes")
    private Integer nombrePretsPretes;
}
