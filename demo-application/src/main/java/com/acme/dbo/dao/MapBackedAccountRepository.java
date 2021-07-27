package com.acme.dbo.dao;

import com.acme.dbo.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapBackedAccountRepository implements AccountRepository {
    private static final Logger log = LoggerFactory.getLogger(MapBackedAccountRepository.class);
    private final Map<Integer, Account> accounts;

    public MapBackedAccountRepository(int initialCapacity) {
        accounts = new HashMap<>(initialCapacity);
        log.debug("Created MapBackedAccountRepository with initial capacity {}", initialCapacity);
    }

    @Override
    public Account create(Account accountData) {
//        Account newAccount = new Account(
//            accounts.isEmpty() ? 0 : Collections.max(accounts.keySet()) + 1,
//            accountData.getAmount()
//        );
        if(accountData == null) {
            throw new RuntimeException("Cannot create null account");
        }
        final int newAccountId = accountData.getId();
        if (accounts.containsKey(newAccountId)) {
            if(!accounts.get(newAccountId).getAmount().equals(accountData.getAmount())) {
                throw new RuntimeException("Account with ID: " + accountData.getId() + " already exists with another amount.");
            } else {
                return accounts.get(accountData.getId());
            }
        }

        accounts.put(newAccountId, new Account(accountData));
        return accounts.get(newAccountId);
    }

    @Override
    public Account findById(Integer id) {
        return accounts.get(id);
    }

    @Override
    public Collection<Account> findAll() {
        return accounts.values();
    }
}
