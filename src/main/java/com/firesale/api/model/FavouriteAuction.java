package com.firesale.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class FavouriteAuction extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
