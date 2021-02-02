package com.firesale.api.repository;

import com.firesale.api.model.FavouriteAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FavouriteAuctionRepository extends JpaRepository<FavouriteAuction, Long> {
    @Query("select fa from FavouriteAuction fa inner join fa.auction a inner join fa.user u where a.id = :auction and u.id = :user")
    Optional<FavouriteAuction> findByAuctionIdAndUserId(Long auction, Long user);

}
