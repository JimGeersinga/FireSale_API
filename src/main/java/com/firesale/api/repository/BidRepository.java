package com.firesale.api.repository;

import com.firesale.api.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionId(long auction);

    List<Bid> findByUserId(long user);
}
