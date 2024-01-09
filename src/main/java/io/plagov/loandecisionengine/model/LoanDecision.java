package io.plagov.loandecisionengine.model;

public record LoanDecision(
        Status status,
        String message
) {
}
