package com.example.financiapro.controller;

import com.example.financiapro.dto.BudgetItemDto;
import com.example.financiapro.mapper.BudgetItemMapper;
import com.example.financiapro.entity.BudgetItem;
import com.example.financiapro.service.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budget-items")
@RequiredArgsConstructor
public class BudgetItemController {

    private final BudgetItemService budgetItemService;
    private final BudgetItemMapper budgetItemMapper;

    @GetMapping
    public ResponseEntity<List<BudgetItemDto>> getAllBudgetItems() {
        List<BudgetItem> budgetItems = budgetItemService.findAll();
        List<BudgetItemDto> budgetItemDtos = budgetItems.stream()
                .map(budgetItemMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetItemDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetItemDto> getBudgetItemById(@PathVariable Long id) {
        Optional<BudgetItem> budgetItem = budgetItemService.findById(id);
        return budgetItem.map(bi -> ResponseEntity.ok(budgetItemMapper.toDto(bi)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetItemDto>> getBudgetItemsByUserId(@PathVariable Long userId) {
        List<BudgetItem> budgetItems = budgetItemService.findByUserId(userId);
        List<BudgetItemDto> budgetItemDtos = budgetItems.stream()
                .map(budgetItemMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetItemDtos);
    }

    @PostMapping
    public ResponseEntity<BudgetItemDto> createBudgetItem(@RequestBody BudgetItemDto budgetItemDto) {
        BudgetItem budgetItem = budgetItemService.createBudgetItem(budgetItemDto);
        return ResponseEntity.ok(budgetItemMapper.toDto(budgetItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetItem(@PathVariable Long id) {
        if (budgetItemService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        budgetItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
