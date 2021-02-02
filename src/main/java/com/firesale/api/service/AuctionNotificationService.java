package com.firesale.api.service;


import com.firesale.api.dto.ResponseType;
import com.firesale.api.model.Auction;
import com.firesale.api.model.AuctionStatus;
import com.firesale.api.model.Bid;
import com.firesale.api.repository.AuctionRepository;
import com.firesale.api.dto.WebsocketAuctionMessage;
import com.firesale.api.dto.bid.BidDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class AuctionNotificationService {
    private final SimpMessagingTemplate template;
    private final AuctionRepository auctionRepository;

    public void sendBidNotification(@DestinationVariable("auctionId") long auctionId, BidDTO bid) {
        template.convertAndSend("/rt-auction/updates/" + auctionId, new WebsocketAuctionMessage<>(ResponseType.BID_PLACED, bid, bid.getUserId(), bid.getCreated()));
    }

    public void sendStatusNotification(@DestinationVariable("auctionId") long auctionId, AuctionStatus auctionStatus) {
        template.convertAndSend("/rt-auction/updates/" + auctionId, new WebsocketAuctionMessage<>(ResponseType.UPDATED, auctionStatus, null, LocalDateTime.now()));
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional(readOnly = false)
    public void closeAuction() {
        var auctions = auctionRepository.getFinalizedAuctions();
        for (Auction auction : auctions) {
            if (!auction.getBids().isEmpty()) {
                var finalBid = Collections.max(auction.getBids(), Comparator.comparing(Bid::getValue));
                auction.setFinalBid(finalBid);
            }

            auction.setStatus(AuctionStatus.CLOSED);

            auctionRepository.save(auction);

            sendStatusNotification(auction.getId(), auction.getStatus());
        }
    }
}
