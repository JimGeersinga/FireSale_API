package com.firesale.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Entity
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Boolean isLocked;

    @OneToOne(cascade = CascadeType.REMOVE, optional = false)
    private Address address;

    @OneToOne(cascade = CascadeType.REMOVE, optional = true)
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne()
    private Image avatar;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "receiver")
    private Collection<Review> incomingReviews;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "reviewer")
    private Collection<Review> outgoingReviews;

    @OneToMany(mappedBy = "user")
    private Collection<Auction> auctions;

    @OneToMany(mappedBy = "user")
    private Collection<Bid> bids;
}