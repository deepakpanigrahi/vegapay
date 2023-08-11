package com.vegapay.creditapp.service;

import com.vegapay.creditapp.model.Account;
import com.vegapay.creditapp.model.LimitOffer;
import com.vegapay.creditapp.model.OfferStatus;
import com.vegapay.creditapp.repository.AccountRepository;
import com.vegapay.creditapp.repository.LimitOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LimitOfferService {

    private final AccountRepository accountRepository;
    private final LimitOfferRepository limitOfferRepository;

    public List<LimitOffer> fetchActiveLimitOffers(UUID accountId, Timestamp activeDate) {

        if (activeDate == null) {
            activeDate = new Timestamp(System.currentTimeMillis());
        }
        Predicate<LimitOffer> status = limitOffer -> limitOffer.getOfferStatus() == OfferStatus.PENDING;
        Timestamp finalActiveDate = activeDate;
        Predicate<LimitOffer> date = limitOffer -> limitOffer.getOfferActivationTime().after(finalActiveDate)
                && limitOffer.getOfferExpiryTime().before(finalActiveDate);

        Account getAccountDetails = accountRepository.getReferenceById(accountId);
        List<LimitOffer> getOffers = getAccountDetails.getLimitOffers();
        return getOffers.stream().filter(status).filter(date).collect(Collectors.toList());
    }

    public LimitOffer updateLimitOffer(UUID offerid, OfferStatus status) {
        LimitOffer offer = limitOfferRepository.getReferenceById(offerid);
        offer.setOfferStatus(status);
        return limitOfferRepository.save(offer);
    }
}
