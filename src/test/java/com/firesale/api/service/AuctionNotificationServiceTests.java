package com.firesale.api.service;

import com.firesale.api.dto.WebsocketAuctionMessage;
import com.firesale.api.dto.bid.BidDTO;
import com.firesale.api.model.Auction;
import com.firesale.api.model.AuctionStatus;
import com.firesale.api.model.Bid;
import com.firesale.api.repository.AuctionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionNotificationServiceTests {
    @Mock
    private SimpMessagingTemplate template;
    @Mock
    private AuctionRepository auctionRepository;
    @InjectMocks
    private AuctionNotificationService auctionNotificationService;

    @Test
    @DisplayName("Test sendBidNotification Success")
    void sendBidNotification() {
        // Setup mock repository
        BidDTO dto = this.getBid();
        doNothing().when(template).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));

        // Execute service call
        auctionNotificationService.sendBidNotification(1L, dto);

        // Assert response
        verify(template).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));
    }

    @Test
    @DisplayName("Test sendStatusNotification Success")
    void sendStatusNotification() {
        // Setup mock repository
        doNothing().when(template).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));

        // Execute service call
        auctionNotificationService.sendStatusNotification(1L, AuctionStatus.READY);

        // Assert response
        verify(template).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));
    }

    @Test
    @DisplayName("Test closeAuction Success")
    void closeAuction() {
        // Setup mock repository
        var auctions = this.getAuctions();
        doNothing().when(template).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));
        when(auctionRepository.save(any(Auction.class))).thenAnswer((i)->i.getArguments()[0]);
        doReturn(auctions).when(auctionRepository).getFinalizedAuctions();
        // Execute service call
        auctionNotificationService.closeAuction();
        // Assert response
        verify(template, times(2)).convertAndSend(any(String.class), any(WebsocketAuctionMessage.class));
        verify(auctionRepository, times(2)).save(any(Auction.class));
    }


    private BidDTO getBid()
    {
        BidDTO dto = new BidDTO();
        dto.setUserId(1L);
        dto.setValue(10d);
        dto.setUserName("tests");
        return dto;
    }
    private List<Auction> getAuctions()
    {
        ArrayList<Auction> auctions = new ArrayList<>();
        Auction auction = new Auction();
        Bid b = new Bid();
        b.setAuction(auction);
        b.setValue(10d);
        auction.setBids(new ArrayList<>());
        auction.getBids().add(b);
        auction.setId(1L);
        auctions.add(auction);
        Auction auction2 = new Auction();
        auction2.setBids(new ArrayList<>());
        auction2.setId(2L);
        auctions.add(auction2);
        return auctions;
    }


}
