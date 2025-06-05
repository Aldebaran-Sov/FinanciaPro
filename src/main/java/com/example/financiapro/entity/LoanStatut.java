package com.example.financiapro.entity;

public enum LoanStatut {
    PENDING("En attente"),
    ACCEPTED("Accepter"),
    REFUSED("Refuser"),
    REPAID("Payer");

    private final String label;

    LoanStatut(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
