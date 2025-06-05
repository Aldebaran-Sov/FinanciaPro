package com.example.financiapro.entity;

public enum BudgetType {
    INCOME("Revenus"),
    EXPENSE("Dépenses");

    private final String label;

    BudgetType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
