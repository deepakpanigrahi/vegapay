package com.vegapay.creditapp.bootstrap;

import com.vegapay.creditapp.model.Account;
import com.vegapay.creditapp.model.LimitType;
import com.vegapay.creditapp.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
public class Bootstrap implements CommandLineRunner {
    AccountService accountService;

    public Bootstrap(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        Account account = new Account();
        account.setAccountLimit(100L);
        account.setLastAccountLimit(account.getAccountLimit());
        account.setAccountLimitUpdateTime(Timestamp.from(Instant.now()));
        account.setCustomerId(UUID.randomUUID());
        Account saved = accountService.createAccount(account);
        accountService.createLimitOffer(saved.getId(), LimitType.ACCOUNT_LIMIT, 100L);
    }
}
