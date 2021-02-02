package com.firesale.api.service;

import com.firesale.api.exception.CreateBidException;
import com.firesale.api.model.Auction;
import com.firesale.api.model.AuctionStatus;
import com.firesale.api.model.Bid;
import com.firesale.api.repository.BidRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTests {

    @Mock
    private BidRepository bidRepository;
    @InjectMocks
    private BidService bidService;

    @Test
    @DisplayName("Test getForAuction Success")
    void getForAuction() {
        // Setup mock repository
        var bids = this.getBids();
        doReturn(bids).when(bidRepository).findByAuctionId(any(Long.class));

        // Execute service call
        var returned = bidService.getForAuction(1L);

        // Assert response
        verify(bidRepository).findByAuctionId(any(Long.class));
        Assertions.assertSame(returned, bids, "Incorrect bids returned");
    }


    @Test
    @DisplayName("Test create Success")
    void create() {
        // Setup mock repository
        var bid = this.getBidCorrect();
        var bids = this.getBids();

        when(bidRepository.save(any(Bid.class))).thenAnswer((answer) -> answer.getArguments()[0]);
        doReturn(bids).when(bidRepository).findByAuctionId(any(Long.class));
        // Execute service call
        var returned = bidService.create(bid);

        // Assert response
        verify(bidRepository).save(bid);
        Assertions.assertSame(bid.getId(), returned.getId(), "Not the same bid is returned");
    }

    @Test
    @DisplayName("Test create AUCTION_ALREADY_COMPLETED")
    void createStatusFailure() {
        // Setup mock repository
        var bid = this.getBidInCorrectStatus();
        var expected = "Auction already completed";
        // Execute service call
        var returned = assertThrows(CreateBidException.class, () ->bidService.create(bid));
        var actual = returned.getMessage();
        // Assert response
        Assertions.assertSame(actual, expected, "Error message is not correct");
    }

    @Test
    @DisplayName("Test create BID_TOO_LOW")
    void createBidFailure() {
        // Setup mock repository
        var bid = this.getBidInCorrect();

        doReturn(this.getBids()).when(bidRepository).findByAuctionId(any(Long.class));


        var expected = "Bid to low";
        // Execute service call
        var returned = assertThrows(CreateBidException.class, () ->bidService.create(bid));
        var actual = returned.getMessage();
        // Assert response
        Assertions.assertSame(actual, expected, "Error message is not correct");
    }



    private Bid getBidCorrect()
    {
        Bid b = new Bid();
        b.setId(15L);
        b.setValue(15d);
        b.setAuction(this.getAuction());
        return b;
    }

    private Bid getBidInCorrect()
    {
        Bid b = new Bid();
        b.setId(15L);

        b.setValue(5d);
        b.setAuction(this.getAuction());
        return b;
    }

    private Bid getBidInCorrectStatus()
    {
        Bid b = new Bid();
        b.setId(15L);

        b.setValue(5d);
        var auction = this.getAuction();
        auction.setStatus(AuctionStatus.CLOSED);
        b.setAuction(auction);
        return b;
    }

    private Auction getAuction()
    {
        Auction auction = new Auction();
        auction.setStatus(AuctionStatus.READY);
        auction.setId(1L);
        return auction;
    }

    private List<Bid> getBids()
    {
        var bids = new ArrayList<Bid>();
        Bid b = new Bid();
        b.setValue(10d);
        bids.add(b);
        Bid b2 = new Bid();
        b2.setValue(11d);
        bids.add(b2);
        Bid b3 = new Bid();
        b3.setValue(13d);
        bids.add(b3);
        return bids;
    }



}
