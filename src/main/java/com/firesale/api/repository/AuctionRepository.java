package com.firesale.api.repository;

import com.firesale.api.model.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT a FROM Auction a WHERE a.status = 'READY' AND a.endDate > current_timestamp AND a.user.id = :id AND a.isDeleted = false order by a.endDate asc")
    List<Auction> findActiveAuctionsByUserId(@Param("id") long userId);

    @Query(value = "SELECT a FROM Auction a WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp  order by a.endDate asc")
    Page<Auction> findActiveAuctions(Pageable pageable);

    @Query(value = "SELECT a FROM Auction a WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND a.isFeatured = false order by a.endDate asc")
    Page<Auction> findActiveAuctionsByIsFeaturedFalse(Pageable pageable);

    @Query(value = "SELECT a FROM Auction a WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND a.isFeatured = true  order by a.endDate asc")
    List<Auction> findActiveAuctionsByIsFeaturedTrue();

    @Query("SELECT distinct a FROM Auction a join a.categories c WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND c.id in :categories  order by a.endDate asc")
    List<Auction> findAuctionsByCategories(long[] categories);

    @Query("SELECT distinct a FROM Auction a join a.tags t WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND t.name in :tags  order by a.endDate asc")
    List<Auction> findAuctionsByTags(String[] tags);


    @Query("SELECT distinct a FROM Auction a join FavouriteAuction fa on fa.auction.id = a.id WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND fa.user.id = :user  order by a.endDate asc")
    List<Auction> findAuctionsByFavourite(Long user);

    @Query("SELECT distinct a FROM Auction a left join a.bids b WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND b.user.id = :user  order by a.endDate asc")
    List<Auction> findActiveByUserBid(Long user);

    @Query("SELECT distinct a FROM Auction a WHERE a.status = 'READY' AND a.isDeleted = false AND a.startDate <= current_timestamp AND a.endDate > current_timestamp AND a.name LIKE CONCAT('%',:name,'%')  order by a.endDate asc")
    List<Auction> findAuctionsByNameLike(String name);

    @Query("SELECT distinct a FROM Auction a join a.categories c WHERE a.status = 'READY' AND a.isDeleted = false AND "
            + "a.startDate <= current_timestamp AND a.endDate > current_timestamp AND "
            + "a.name LIKE CONCAT('%',:name,'%') AND c.id in :categories  order by a.endDate asc")
    List<Auction> findAuctionsByCategoriesLikeAndNameLike(long[] categories, String name);

    @Query("SELECT distinct a FROM Auction a join a.tags t WHERE a.status = 'READY' AND a.isDeleted = false AND "
            + "a.startDate <= current_timestamp AND a.endDate > current_timestamp AND "
            + "a.name LIKE CONCAT('%',:name,'%') AND t.name in :tags  order by a.endDate asc")
    List<Auction> findAuctionsByTagsLikeAndNameLike(String[] tags, String name);

    @Query("SELECT distinct a FROM Auction a join a.tags t join a.categories c WHERE a.status = 'READY' AND a.isDeleted = false AND "
            + "a.startDate <= current_timestamp AND a.endDate > current_timestamp AND "
            + "a.name LIKE CONCAT('%',:name,'%') AND t.name in :tags AND c.id in :categories  order by a.endDate asc")
    List<Auction> findAuctionsByTagsLikeAndCategoriesANDNameLike(String[] tags, long[] categories, String name);

    @Query("SELECT distinct a FROM Auction a join a.tags t join a.categories c WHERE a.status = 'READY' AND a.isDeleted = false AND "
            + "a.startDate <= current_timestamp AND a.endDate > current_timestamp AND "
            + "t.name in :tags AND c.id in :categories  order by a.endDate asc")
    List<Auction> findAuctionsByTagsLikeAndCategoriesLike(String[] tags, long[] categories);

    List<Auction> findByUserIdAndIsDeletedFalseOrderByEndDateDesc(long userId);

    @Query("SELECT a FROM Auction a WHERE a.status = 'READY' AND a.endDate <= current_timestamp AND a.isDeleted = false ")
    List<Auction> getFinalizedAuctions();

    @Query("SELECT distinct a FROM Auction a join a.finalBid f WHERE a.status = 'CLOSED' AND a.isDeleted = false and f.user.id = :user order by a.endDate desc")
    List<Auction> findWonByUserBid(Long user);
}
