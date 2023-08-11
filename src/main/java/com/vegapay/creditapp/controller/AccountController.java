package com.vegapay.creditapp.controller;

import com.vegapay.creditapp.model.Account;
import com.vegapay.creditapp.model.LimitOffer;
import com.vegapay.creditapp.model.LimitType;
import com.vegapay.creditapp.model.OfferStatus;
import com.vegapay.creditapp.service.AccountService;
import com.vegapay.creditapp.service.LimitOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private LimitOfferService limitOfferService;

    @PostMapping(path = "/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) throws Exception {
        return Optional.ofNullable(accountService.createAccount(account))
                .map(a -> new ResponseEntity<>(a, HttpStatus.CREATED))
                .orElseThrow(() -> new Exception("New account creation failed"));
    }

    @GetMapping(path = "/getAccount/{accountId}")
    public ResponseEntity<Account> getAccountDetails(@PathVariable UUID accountId) {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @PutMapping(path = "/createOffers/{accountId}/{limitType}/{updateLimit}")
    public ResponseEntity<LimitOffer> createLimitOffer(@PathVariable UUID accountId, @PathVariable LimitType limitType, @PathVariable Long updateLimit) {
        return new ResponseEntity<>(accountService.createLimitOffer(accountId, limitType, updateLimit), HttpStatus.CREATED);
    }

    @GetMapping(path = "/fetchLimitOffers/{accountId}/{activationDate}")
    public ResponseEntity<List<LimitOffer>> getLimitOffers(@PathVariable UUID accountId, @PathVariable Timestamp activationDate) {
        return new ResponseEntity<>(limitOfferService.fetchActiveLimitOffers(accountId, activationDate), HttpStatus.OK);
    }

    @PutMapping(path = "/updateOffer/{offerId}/{status}")
    public ResponseEntity<LimitOffer> updateLimitOffer(@PathVariable UUID offerId, @PathVariable OfferStatus status) {
        return new ResponseEntity<>(limitOfferService.updateLimitOffer(offerId, status), HttpStatus.CREATED);
    }
}
