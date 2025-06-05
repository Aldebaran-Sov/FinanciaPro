package com.example.financiapro.mapper;

import com.example.financiapro.dto.BudgetItemDto;
import com.example.financiapro.entity.BudgetItem;
import org.springframework.stereotype.Component;

@Component
public class BudgetItemMapper {

    public BudgetItemDto toDto(BudgetItem budgetItem) {
        if (budgetItem == null) return null;

        BudgetItemDto dto = new BudgetItemDto();
        dto.setId(budgetItem.getId());
        dto.setDescription(budgetItem.getDescription());
        dto.setMontant(budgetItem.getMontant());
        dto.setDate(budgetItem.getDate());
        dto.setType(budgetItem.getType());

        // Infos de l'utilisateur associé
        if (budgetItem.getUser() != null) {
            dto.setUserId(budgetItem.getUser().getId());
            dto.setUserNom(budgetItem.getUser().getNom());
            dto.setUserPrenom(budgetItem.getUser().getPrenom());
        }

        return dto;
    }

    public BudgetItem toEntity(BudgetItemDto dto) {
        if (dto == null) return null;

        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(dto.getId());
        budgetItem.setDescription(dto.getDescription());
        budgetItem.setMontant(dto.getMontant());
        budgetItem.setDate(dto.getDate());
        budgetItem.setType(dto.getType());

        // Note: L'association User sera gérée par le service
        return budgetItem;
    }
}
