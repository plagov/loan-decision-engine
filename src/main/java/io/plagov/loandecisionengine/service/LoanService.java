package io.plagov.loandecisionengine.service;

import io.plagov.loandecisionengine.client.UserRegistryApi;
import io.plagov.loandecisionengine.exception.LoanInvalidInputException;
import io.plagov.loandecisionengine.model.LoanApplication;
import io.plagov.loandecisionengine.model.LoanDecision;
import org.springframework.stereotype.Component;

import static io.plagov.loandecisionengine.model.Status.REJECTED;

@Component
public class LoanService {

    private final UserRegistryApi userRegistry;

    public LoanService(UserRegistryApi userRegistry) {
        this.userRegistry = userRegistry;
    }

    public LoanDecision getLoanDecision(LoanApplication loanApplication) {
        validateApplicationInput(loanApplication);
        var user = userRegistry.findUser(loanApplication.personalCode());
        if (user.hasDebt()) {
            return new LoanDecision(REJECTED, "You loan application is rejected because you have a debt");
        }
        return null;
    }

    private void validateApplicationInput(LoanApplication loanApplication) {
        if (loanApplication.loanAmount() < 2_000 || loanApplication.loanAmount() > 10_000) {
            throw new LoanInvalidInputException("The loan amount must be within the 2000 and 10000 EUR range");
        }
        if (loanApplication.loanPeriod() < 12 || loanApplication.loanPeriod() > 60) {
            throw new LoanInvalidInputException("The loan period must be within 12 and 60 months range");
        }
    }
}
