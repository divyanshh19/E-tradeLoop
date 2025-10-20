package com.etradeloop.controller;

import com.etradeloop.model.Item;
import com.etradeloop.model.TradeRequest;
import com.etradeloop.model.User;
import com.etradeloop.repository.ItemRepository;
import com.etradeloop.repository.TradeRequestRepository;
import com.etradeloop.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "*")
public class TradeController {

    private final TradeRequestRepository tradeRepo;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    public TradeController(TradeRequestRepository tradeRepo, ItemRepository itemRepo, UserRepository userRepo) {
        this.tradeRepo = tradeRepo;
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<TradeRequest> getAllTrades() {
        return tradeRepo.findAll();
    }

    @PostMapping("/propose")
    public TradeRequest proposeTrade(@RequestBody Map<String, Long> req, @AuthenticationPrincipal User user) {
        Long proposedItemId = req.get("proposedItemId");
        Long requestedItemId = req.get("requestedItemId");

        Item proposedItem = itemRepo.findById(proposedItemId).orElseThrow();
        Item requestedItem = itemRepo.findById(requestedItemId).orElseThrow();

        TradeRequest trade = new TradeRequest();
        trade.setProposer(user);
        trade.setProposedItem(proposedItem);
        trade.setRequestedItem(requestedItem);
        trade.setStatus("PENDING");

        return tradeRepo.save(trade);
    }

    @PostMapping("/{tradeId}/accept")
    public TradeRequest acceptTrade(@PathVariable Long tradeId) {
        TradeRequest trade = tradeRepo.findById(tradeId).orElseThrow();

        // Swap ownership
        User owner1 = trade.getProposer();
        User owner2 = trade.getRequestedItem().getOwner();

        Item item1 = trade.getProposedItem();
        Item item2 = trade.getRequestedItem();

        item1.setOwner(owner2);
        item2.setOwner(owner1);

        itemRepo.save(item1);
        itemRepo.save(item2);

        trade.setStatus("ACCEPTED");
        return tradeRepo.save(trade);
    }

    @PostMapping("/{tradeId}/reject")
    public TradeRequest rejectTrade(@PathVariable Long tradeId) {
        TradeRequest trade = tradeRepo.findById(tradeId).orElseThrow();
        trade.setStatus("REJECTED");
        return tradeRepo.save(trade);
    }
}
