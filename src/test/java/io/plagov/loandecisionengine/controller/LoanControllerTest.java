package io.plagov.loandecisionengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
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
                """  ;
        mockMvc.perform(MockMvcRequestBuilders.post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("status", is("REJECTED")),
                        jsonPath("message", is("You loan application is rejected because you have a debt"))
                );
    }
}
