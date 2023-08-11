package com.vegapay.creditapp.service;

import com.vegapay.creditapp.model.Account;
import com.vegapay.creditapp.model.LimitOffer;
import com.vegapay.creditapp.model.LimitType;
import com.vegapay.creditapp.model.OfferStatus;
import com.vegapay.creditapp.repository.AccountRepository;
import com.vegapay.creditapp.repository.LimitOfferRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final LimitOfferRepository limitOfferRepository;

    @Getter
    @Value("${offer.activation.timeindays}")
    private int offerActivationInDays;

    public Account createAccount(Account account) {
        account.setId(UUID.randomUUID());
        Account createdAccount = accountRepository.save(account);
        log.info("Account created with id -- {}", createdAccount.getId());
        return createdAccount;
    }

    public Account getAccountDetails(UUID accountId) {
        log.info("Looking for account details with id -- {}", accountId);
        return accountRepository.findById(accountId).get();
    }

    @Transactional
    public LimitOffer createLimitOffer(UUID accountId, LimitType limitType, Long updateLimit) {
        log.info("Looking for account with id {} for create limit offer", accountId);
        Account account = getAccountDetails(accountId);
        Account updatedAccount = null;
        if (account != null) {
            log.info("Found account details for id {}" , accountId);
            account.setLimitType(limitType);
            account.setAccountLimit(account.getAccountLimit() + updateLimit);
            account.setAccountLimitUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (limitType == LimitType.ACCOUNT_LIMIT) {
                account.setAccountLimitUpdateTime(new Timestamp(System.currentTimeMillis()));
            } else if (limitType == LimitType.PER_TRANSACTION_LIMIT) {
                account.setPerTransactionLimitUpdateTime(new Timestamp(System.currentTimeMillis()));
            }
            updatedAccount = accountRepository.save(account);
        }
        Timestamp offerActivationTime = new Timestamp(System.currentTimeMillis());
        Timestamp offerExpiryTime = Timestamp.valueOf(offerActivationTime.toLocalDateTime().plusDays(getOfferActivationInDays()));
        LimitOffer newLimitOffer = new LimitOffer(UUID.randomUUID(),
                updatedAccount, updatedAccount.getLimitType(),
                updatedAccount.getAccountLimit(), offerActivationTime, offerExpiryTime, OfferStatus.PENDING);
        LimitOffer saved = limitOfferRepository.saveAndFlush(newLimitOffer);
        log.info("New limit offer created with id {}", saved.getId());
        return saved;
    }


}
