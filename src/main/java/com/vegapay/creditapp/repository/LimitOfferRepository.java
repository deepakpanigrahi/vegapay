package com.vegapay.creditapp.repository;

import com.vegapay.creditapp.model.LimitOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LimitOfferRepository extends JpaRepository<LimitOffer, UUID> {

    List<LimitOffer> findLimitOffersById(UUID offerId);
}
