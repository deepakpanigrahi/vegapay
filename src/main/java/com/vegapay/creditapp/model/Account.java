package com.vegapay.creditapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private UUID id;

    private UUID customerId;
    private Long accountLimit;
    private Long perTransactionLimit;
    private Long lastAccountLimit;
    private Long lastPerTransactionLimit;
    private Timestamp accountLimitUpdateTime;
    private Timestamp perTransactionLimitUpdateTime;

    @Enumerated(value = EnumType.STRING)
    private LimitType limitType;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LimitOffer> limitOffers = new ArrayList<>();
}
