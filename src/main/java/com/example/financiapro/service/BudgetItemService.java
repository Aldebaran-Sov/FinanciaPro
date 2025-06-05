package com.example.financiapro.service;

import com.example.financiapro.dto.BudgetItemDto;
import com.example.financiapro.entity.BudgetItem;
import com.example.financiapro.entity.User;
import com.example.financiapro.repository.BudgetItemRepository;
import com.example.financiapro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;
    private final UserRepository userRepository;

    public List<BudgetItem> findAll() {
        return budgetItemRepository.findAll();
    }

    public Optional<BudgetItem> findById(Long id) {
        return budgetItemRepository.findById(id);
    }

    public List<BudgetItem> findByUserId(Long userId) {
        return budgetItemRepository.findByUserId(userId);
    }

    public BudgetItem createBudgetItem(BudgetItemDto dto) {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setDescription(dto.getDescription());
        budgetItem.setMontant(dto.getMontant());
        budgetItem.setDate(dto.getDate());
        budgetItem.setType(dto.getType());

        // Association avec l'utilisateur
        if (dto.getUserId() != null) {
            Optional<User> user = userRepository.findById(dto.getUserId());
            user.ifPresent(budgetItem::setUser);
        }

        return budgetItemRepository.save(budgetItem);
    }

    public void deleteById(Long id) {
        budgetItemRepository.deleteById(id);
    }
}
