package io.plagov.loandecisionengine.service;

import io.plagov.loandecisionengine.client.UserProfile;
import io.plagov.loandecisionengine.client.UserRegistryApi;
import io.plagov.loandecisionengine.exception.LoanInvalidInputException;
import io.plagov.loandecisionengine.model.LoanApplication;
import io.plagov.loandecisionengine.model.LoanDecision;
import org.springframework.stereotype.Component;

import static io.plagov.loandecisionengine.model.Status.APPROVED;
import static io.plagov.loandecisionengine.model.Status.REJECTED;

@Component
public class LoanService {

    private final UserRegistryApi userRegistry;

    private static final double MIN_AMOUNT = 2_000.00;
    private static final double MAX_AMOUNT = 10_000.00;

    private static final int MIN_PERIOD = 12;
    private static final int MAX_PERIOD = 60;

    public LoanService(UserRegistryApi userRegistry) {
        this.userRegistry = userRegistry;
    }

    public LoanDecision getLoanDecision(LoanApplication loanApplication) {
        validateApplicationInput(loanApplication);
        var user = userRegistry.findUser(loanApplication.personalCode());
        if (user.hasDebt()) {
            return new LoanDecision(
                    REJECTED,
                    "You loan application is rejected because you have a debt",
                    null,
                    null);
        }

        var creditScore = getCreditScoreForUser(user, loanApplication);
        if (creditScore >= 1) {
            return getLoanDecisionForHighCreditScore(user, loanApplication);
        } else {
            return getLoanDecisionForLowCreditScore(user, loanApplication);
        }
    }

    private LoanDecision getLoanDecisionForLowCreditScore(UserProfile user, LoanApplication loanApplication) {
        var amount = (double) user.creditModifier() * loanApplication.loanPeriod();
        if (amount < MIN_AMOUNT) {
            var period = (int) Math.round(MIN_AMOUNT / user.creditModifier());
            return new LoanDecision(APPROVED,
                    "You loan application has been approved",
                    MIN_AMOUNT,
                    period);
        } else {
            return new LoanDecision(APPROVED,
                    "You loan application has been approved",
                    amount,
                    loanApplication.loanPeriod());

        }
    }

    private LoanDecision getLoanDecisionForHighCreditScore(UserProfile user, LoanApplication loanApplication) {
        var maxIssuedAmount = user.creditModifier() / (1.0 / loanApplication.loanPeriod());
        var decisionAmount = Math.min(maxIssuedAmount, MAX_AMOUNT);
        return new LoanDecision(APPROVED,
                "You loan application has been approved",
                decisionAmount,
                loanApplication.loanPeriod());
    }

    private double getCreditScoreForUser(UserProfile user, LoanApplication loanApplication) {
        return (user.creditModifier() / loanApplication.loanAmount()) * loanApplication.loanPeriod();
    }

    private void validateApplicationInput(LoanApplication loanApplication) {
        if (loanApplication.loanAmount() < MIN_AMOUNT || loanApplication.loanAmount() > MAX_AMOUNT) {
            throw new LoanInvalidInputException("The loan amount must be within the 2000 and 10000 EUR range");
        }
        if (loanApplication.loanPeriod() < MIN_PERIOD || loanApplication.loanPeriod() > MAX_PERIOD) {
            throw new LoanInvalidInputException("The loan period must be within 12 and 60 months range");
        }
    }
}
