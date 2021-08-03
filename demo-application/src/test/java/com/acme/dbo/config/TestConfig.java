package com.acme.dbo.config;

import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.dao.MapBackedAccountRepository;
import com.acme.dbo.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Collection;

import static java.util.Collections.EMPTY_SET;
import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("com.acme.dbo")
@PropertySource("classpath:application.properties")
@Import(Config.class)
public class TestConfig {
    @Bean @Primary
    public AccountRepository accountRepositoryStub(@Value("${accounts.repo.init-capacity}") int initCapacity) {
        return mock(AccountRepository.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}