package com.vegapay.creditapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LimitOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    @JsonBackReference
    private Account account;

    private LimitType limitType;
    private Long newLimit;
    private Timestamp offerActivationTime;
    private Timestamp offerExpiryTime;

    @Enumerated(value = EnumType.STRING)
    private OfferStatus offerStatus;
}
