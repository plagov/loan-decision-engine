package io.plagov.loandecisionengine.service;

import io.plagov.loandecisionengine.client.UserRegistryApi;
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
        var user = userRegistry.findUser(loanApplication.personalCode());
        if (user.hasDebt()) {
            return new LoanDecision(REJECTED, "You loan application is rejected because you have a debt");
        }
        return null;
    }
}
