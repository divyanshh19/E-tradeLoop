package com.etradeloop.repository;

import com.etradeloop.model.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {
    List<TradeRequest> findByProposerId(Long proposerId);
    List<TradeRequest> findByRequestedItemOwnerId(Long ownerId); // Trades where the user is the requested item's owner
    List<TradeRequest> findByStatus(String status);
}
