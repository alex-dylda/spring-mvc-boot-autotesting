package com.acme.dbo;

import com.acme.config.TestConfig;
import com.acme.dbo.controller.AccountController;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
@WebAppConfiguration
public class AccountCrudWithMvcIT {
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountRepository accountRepositoryStub;
    private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @BeforeEach
    public void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetNoAccountsWhenNoCreated() throws Exception {
        mockMvc.perform(get("/api/account"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test @DirtiesContext
    public void shouldGetAccountsWhenRepositoryIsNotEmpty() throws Exception {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.singletonList(new Account(1, new BigDecimal("1"))));

        mockMvc.perform(get("/api/account"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(new BigDecimal("1")));
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenRepositoryIsEmpty() throws Exception {
        when(accountRepositoryStub
                .create(new Account(1, new BigDecimal("1.11"))))
                .thenReturn(new Account(1, new BigDecimal("1.11")));

        mockMvc.perform(post("/api/account")
                .content(mapper.writeValueAsString(new Account(1, new BigDecimal("1.11"))))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(new BigDecimal("1.11")));
    }

    @Test @DirtiesContext
    public void shouldFindAccountIfExists() throws Exception {
        when(accountRepositoryStub.findById(1)).thenReturn(new Account(1, new BigDecimal("1")));

        mockMvc.perform(get("/api/account/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(new BigDecimal("1")));

    }
/*
    @Test
    public void shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepositoryStub.findById(1)).thenReturn(null);
        final AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,() -> accountController.findById(1));
        assertEquals(AccountNotFoundException.class, exception.getClass());
        assertEquals("1",exception.getMessage());
    }

 */
}
