package com.acme.dbo;

import com.acme.dbo.config.TestConfig;
import com.acme.dbo.controller.AccountController;
import com.acme.dbo.controller.AccountNotFoundException;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
@WebAppConfiguration
public class AccountCrudIT {
    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountRepository accountRepositoryStub;

    @Test
    public void shouldGetNoAccountsWhenNoCreated() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.emptyList());
        assertTrue(accountController.findAll().isEmpty());
    }

    @Test @DirtiesContext
    public void shouldGetAccountsWhenRepositoryIsNotEmpty() {
        when(accountRepositoryStub.findAll()).thenReturn(asList(new Account(1, new BigDecimal("1"))));
        assertTrue(accountController.findAll().size() == 1);
        assertTrue(accountController.findAll().contains(
                new Account(1, new BigDecimal("1"))));
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenRepositoryIsEmpty() {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);

        verify(accountRepositoryStub).create(newAcc);
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenRepositoryIsNotEmpty() {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);
        final Account newAcc2 = new Account(2, new BigDecimal(200));
        accountController.create(newAcc2);
        verify(accountRepositoryStub).create(newAcc);
        verify(accountRepositoryStub).create(newAcc2);
    }

    @Test
    public void shoudlNotThrowExceptionWhenAccountIsNull() {
        accountController.create(null);
        verify(accountRepositoryStub).create(null);
    }

    @Test
    public void shouldFindAccountIfExists() throws AccountNotFoundException {
        when(accountRepositoryStub.findById(1)).thenReturn(new Account(1, new BigDecimal("1")));
        final Account account = accountController.findById(1);
        assertEquals(1, account.getId());
        assertEquals(new BigDecimal("1"), account.getAmount());
    }

    @Test
    public void shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepositoryStub.findById(1)).thenReturn(null);
        final AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,() -> accountController.findById(1));
        assertEquals(AccountNotFoundException.class, exception.getClass());
        assertEquals("1",exception.getMessage());
    }
}
