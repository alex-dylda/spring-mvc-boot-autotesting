package com.acme.config;

import com.acme.dbo.dao.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

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