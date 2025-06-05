package com.example.financiapro.mapper;

import com.example.financiapro.dto.LoanRequestDto;
import com.example.financiapro.entity.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestMapper {

    public LoanRequestDto toDto(LoanRequest entity) {
        if (entity == null) {
            return null;
        }

        LoanRequestDto dto = new LoanRequestDto();
        dto.setId(entity.getId());
        dto.setMontant(entity.getMontant());
        dto.setTauxInteret(entity.getTauxInteret());
        dto.setDureeEnMois(entity.getDureeEnMois());
        dto.setStatut(entity.getStatut());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateAcceptation(entity.getDateAcceptation());
        dto.setCommentaire(entity.getCommentaire());

        // Informations de l'emprunteur
        if (entity.getBorrower() != null) {
            dto.setBorrowerId(entity.getBorrower().getId());
            dto.setBorrowerNom(entity.getBorrower().getNom());
            dto.setBorrowerPrenom(entity.getBorrower().getPrenom());
        }

        // Informations du prêteur (optionnel)
        if (entity.getLender() != null) {
            dto.setLenderId(entity.getLender().getId());
            dto.setLenderNom(entity.getLender().getNom());
            dto.setLenderPrenom(entity.getLender().getPrenom());
        }

        return dto;
    }

    public LoanRequest toEntity(LoanRequestDto dto) {
        if (dto == null) {
            return null;
        }

        LoanRequest entity = new LoanRequest();
        entity.setId(dto.getId());
        entity.setMontant(dto.getMontant());
        entity.setTauxInteret(dto.getTauxInteret());
        entity.setDureeEnMois(dto.getDureeEnMois());
        entity.setStatut(dto.getStatut());
        entity.setDateCreation(dto.getDateCreation());
        entity.setDateAcceptation(dto.getDateAcceptation());
        entity.setCommentaire(dto.getCommentaire());

        // Note: borrower et lender seront définis dans le service

        return entity;
    }
}
