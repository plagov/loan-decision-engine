package io.plagov.loandecisionengine.controller;

import io.plagov.loandecisionengine.exception.LoanInvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRejectStatus_whenUserHasDebt() throws Exception {
        String payload = """
                {
                    "personalCode": "49002010965",
                    "loanAmount": 5000,
                    "loanPeriod": 24
                }
                """;
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("status", is("REJECTED")),
                        jsonPath("message", is("You loan application is rejected because you have a debt"))
                );
    }

    @ParameterizedTest
    @ValueSource(doubles = {1999.99, 10000.01})
    void shouldThrowValidationException_whenLoanAmountIsOutOfSupportedRange(double loanAmount) throws Exception {
        String payload = """
                {
                    "personalCode": "49002010965",
                    "loanAmount": %s,
                    "loanPeriod": 24
                }
                """.formatted(loanAmount);
        var result = mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertThat(result.getResolvedException())
                .isInstanceOf(LoanInvalidInputException.class)
                .hasMessage("The loan amount must be within the 2000 and 10000 EUR range");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 62})
    void shouldThrowValidationException_whenLoanPeriodIsOutOfSupportedRange(double loanPeriod) throws Exception {
        String payload = """
                {
                    "personalCode": "49002010965",
                    "loanAmount": 5000,
                    "loanPeriod": %s
                }
                """.formatted(loanPeriod);
        var result = mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertThat(result.getResolvedException())
                .isInstanceOf(LoanInvalidInputException.class)
                .hasMessage("The loan period must be within 12 and 60 months range");
    }
}
