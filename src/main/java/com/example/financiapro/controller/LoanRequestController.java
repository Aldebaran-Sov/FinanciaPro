package com.example.financiapro.controller;

import com.example.financiapro.dto.LoanRequestDto;
import com.example.financiapro.entity.LoanRequest;
import com.example.financiapro.service.LoanRequestService;
import com.example.financiapro.mapper.LoanRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan-requests")
@RequiredArgsConstructor
public class LoanRequestController {

    private final LoanRequestService loanRequestService;
    private final LoanRequestMapper loanRequestMapper;

    @GetMapping
    public List<LoanRequestDto> getAllLoanRequests() {
        return loanRequestService.findAll()
                .stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDto> getLoanRequestById(@PathVariable Long id) {
        return loanRequestService.findById(id)
                .map(loanRequestMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/borrower/{borrowerId}")
    public List<LoanRequestDto> getLoanRequestsByBorrower(@PathVariable Long borrowerId) {
        return loanRequestService.findByBorrowerId(borrowerId)
                .stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/lender/{lenderId}")
    public List<LoanRequestDto> getLoanRequestsByLender(@PathVariable Long lenderId) {
        return loanRequestService.findByLenderId(lenderId)
                .stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/pending")
    public List<LoanRequestDto> getPendingLoanRequests() {
        return loanRequestService.findPendingRequests()
                .stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<LoanRequestDto> createLoanRequest(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        try {
            LoanRequest created = loanRequestService.createLoanRequest(loanRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(loanRequestMapper.toDto(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<LoanRequestDto> acceptLoanRequest(@PathVariable Long id, @RequestParam Long lenderId) {
        try {
            LoanRequest accepted = loanRequestService.acceptLoanRequest(id, lenderId);
            return ResponseEntity.ok(loanRequestMapper.toDto(accepted));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LoanRequestDto> rejectLoanRequest(@PathVariable Long id) {
        try {
            LoanRequest rejected = loanRequestService.rejectLoanRequest(id);
            return ResponseEntity.ok(loanRequestMapper.toDto(rejected));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/repaid")
    public ResponseEntity<LoanRequestDto> markAsRepaid(@PathVariable Long id) {
        try {
            LoanRequest repaid = loanRequestService.markAsRepaid(id);
            return ResponseEntity.ok(loanRequestMapper.toDto(repaid));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanRequest(@PathVariable Long id) {
        try {
            loanRequestService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
