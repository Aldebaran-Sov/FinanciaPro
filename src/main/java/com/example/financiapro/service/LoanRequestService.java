package com.example.financiapro.service;

import com.example.financiapro.dto.LoanRequestDto;
import com.example.financiapro.entity.LoanRequest;
import com.example.financiapro.entity.LoanStatut;
import com.example.financiapro.entity.User;
import com.example.financiapro.repository.LoanRequestRepository;
import com.example.financiapro.repository.UserRepository;
import com.example.financiapro.mapper.LoanRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;
    private final LoanRequestMapper loanRequestMapper;

    @Transactional(readOnly = true)
    public List<LoanRequest> findAll() {
        return loanRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<LoanRequest> findById(Long id) {
        return loanRequestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<LoanRequest> findByBorrowerId(Long borrowerId) {
        return loanRequestRepository.findByBorrowerId(borrowerId);
    }

    @Transactional(readOnly = true)
    public List<LoanRequest> findByLenderId(Long lenderId) {
        return loanRequestRepository.findByLenderId(lenderId);
    }

    @Transactional(readOnly = true)
    public List<LoanRequest> findPendingRequests() {
        return loanRequestRepository.findByStatut(LoanStatut.PENDING);
    }

    public LoanRequest createLoanRequest(LoanRequestDto loanRequestDto) {
        User borrower = userRepository.findById(loanRequestDto.getBorrowerId())
                .orElseThrow(() -> new IllegalArgumentException("Emprunteur introuvable avec l'ID: " + loanRequestDto.getBorrowerId()));

        LoanRequest loanRequest = loanRequestMapper.toEntity(loanRequestDto);
        loanRequest.setBorrower(borrower);

        return loanRequestRepository.save(loanRequest);
    }

    public LoanRequest acceptLoanRequest(Long loanRequestId, Long lenderId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt introuvable"));

        if (loanRequest.getStatut() != LoanStatut.PENDING) {
            throw new IllegalStateException("Cette demande ne peut plus être acceptée. Statut actuel: " + loanRequest.getStatut());
        }

        User lender = userRepository.findById(lenderId)
                .orElseThrow(() -> new IllegalArgumentException("Prêteur introuvable avec l'ID: " + lenderId));

        if (lender.getId().equals(loanRequest.getBorrower().getId())) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas se prêter à lui-même");
        }

        loanRequest.setLender(lender);
        loanRequest.setStatut(LoanStatut.ACCEPTED);
        loanRequest.setDateAcceptation(LocalDate.now());

        return loanRequestRepository.save(loanRequest);
    }

    public LoanRequest rejectLoanRequest(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt introuvable"));

        if (loanRequest.getStatut() != LoanStatut.PENDING) {
            throw new IllegalStateException("Cette demande ne peut plus être refusée. Statut actuel: " + loanRequest.getStatut());
        }

        loanRequest.setStatut(LoanStatut.REFUSED);
        return loanRequestRepository.save(loanRequest);
    }

    public LoanRequest markAsRepaid(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt introuvable"));

        if (loanRequest.getStatut() != LoanStatut.ACCEPTED) {
            throw new IllegalStateException("Seuls les prêts acceptés peuvent être marqués comme remboursés");
        }

        loanRequest.setStatut(LoanStatut.REPAID);
        return loanRequestRepository.save(loanRequest);
    }

    public void deleteById(Long id) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande de prêt introuvable"));

        if (loanRequest.getStatut() == LoanStatut.ACCEPTED || loanRequest.getStatut() == LoanStatut.REPAID) {
            throw new IllegalStateException("Impossible de supprimer une demande acceptée ou remboursée");
        }

        loanRequestRepository.deleteById(id);
    }
}
