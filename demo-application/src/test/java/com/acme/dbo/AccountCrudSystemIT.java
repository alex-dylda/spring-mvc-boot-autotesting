package com.acme.dbo;

import com.acme.dbo.config.Config;
import com.acme.dbo.controller.AccountController;
import com.acme.dbo.controller.AccountNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource("classpath:application-test.properties")
@WebAppConfiguration
@Disabled
public class AccountCrudSystemIT {
    @Autowired
    private AccountController accountController;

    @Test
    public void shouldGetNoAccountsWhenNoCreated() {
        assertTrue(accountController.findAll().isEmpty());
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenRepositoryIsEmpty() throws AccountNotFoundException {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);
        final Account createdAccount = accountController.findById(newAcc.getId());
        assertEquals(1, accountController.findAll().size());
        assertEquals(newAcc.getId(), createdAccount.getId());
        assertEquals(newAcc.getAmount(), createdAccount.getAmount());
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenRepositoryIsNotEmpty() throws AccountNotFoundException {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);
        final Account newAcc2 = new Account(2, new BigDecimal(200));
        accountController.create(newAcc2);
        final Account createdAccount = accountController.findById(newAcc2.getId());
        assertEquals(2, accountController.findAll().size());
        assertEquals(newAcc2.getId(), createdAccount.getId());
        assertEquals(newAcc2.getAmount(), createdAccount.getAmount());
    }

    @Test @DirtiesContext
    public void shouldCreateAccountWhenAccountIsAlreadyExistsWithTheSameAmount() throws AccountNotFoundException {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);
        final Account newAcc2 = new Account(1, new BigDecimal(100));
        accountController.create(newAcc2);
        final Account createdAccount = accountController.findById(newAcc2.getId());
        assertEquals(1, accountController.findAll().size());
        assertEquals(newAcc2.getId(), createdAccount.getId());
        assertEquals(newAcc2.getAmount(), createdAccount.getAmount());
    }

    @Test @DirtiesContext
    public void shouldThrowExceptionWhenAccountIsAlreadyExistsWithAnotherAmount() {
        final Account newAcc = new Account(1, new BigDecimal(100));
        accountController.create(newAcc);
        final Account newAcc2 = new Account(1, new BigDecimal(200));
        final RuntimeException exception = assertThrows(RuntimeException.class,() -> accountController.create(newAcc2));
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Account with ID: 1 already exists with another amount.",exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCreatedAccountIsNull() {
        final RuntimeException exception = assertThrows(RuntimeException.class,() -> accountController.create(null));
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Cannot create null account",exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenAccountNotFound() {
        final AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,() -> accountController.findById(1));
        assertEquals(AccountNotFoundException.class, exception.getClass());
        assertEquals("1",exception.getMessage());
    }
}
