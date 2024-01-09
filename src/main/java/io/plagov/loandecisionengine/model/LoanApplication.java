package io.plagov.loandecisionengine.model;

public record LoanApplication(
        String personalCode,
        double loanAmount,
        int loanPeriod
) {
}
