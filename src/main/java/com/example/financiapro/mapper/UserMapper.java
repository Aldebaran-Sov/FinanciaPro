package com.example.financiapro.mapper;

import com.example.financiapro.dto.UserDto;
import com.example.financiapro.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setApiKey(user.getApiKey());

        // Métadonnées
        dto.setNombreBudgetItems(user.getBudgetItems() != null ? user.getBudgetItems().size() : 0);
        //dto.setNombrePretsEmpruntes(user.getLoanRequestsAsBorrower() != null ? user.getLoanRequestsAsBorrower().size() : 0);
        //dto.setNombrePretsPretes(user.getLoanRequestsAsLender() != null ? user.getLoanRequestsAsLender().size() : 0);

        return dto;
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setApiKey(dto.getApiKey());

        return user;
    }
}
