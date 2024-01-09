package io.plagov.loandecisionengine.controller;

import io.plagov.loandecisionengine.model.LoanApplication;
import io.plagov.loandecisionengine.model.LoanDecision;
import io.plagov.loandecisionengine.service.LoanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    public LoanDecision loanDecision(@RequestBody LoanApplication loanApplication) {
        return loanService.getLoanDecision(loanApplication);
    }
}
