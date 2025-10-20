package com.etradeloop.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class TradeRequest {

    // ✅ Getters & setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User proposer;

    @ManyToOne
    private Item proposedItem;

    @ManyToOne
    private Item requestedItem;

    private String status; // "PENDING", "ACCEPTED", "REJECTED"

    // ✅ No-arg constructor (required for JPA + fixes your error)
    public TradeRequest() {}

    // ✅ Optional parameterized constructor
    public TradeRequest(Long id, User proposer, Item proposedItem, Item requestedItem, String status) {
        this.id = id;
        this.proposer = proposer;
        this.proposedItem = proposedItem;
        this.requestedItem = requestedItem;
        this.status = status;
    }

    public void setId(Long id) { this.id = id; }

    public void setProposer(User proposer) { this.proposer = proposer; }

    public void setProposedItem(Item proposedItem) { this.proposedItem = proposedItem; }

    public void setRequestedItem(Item requestedItem) { this.requestedItem = requestedItem; }

    public void setStatus(String status) { this.status = status; }
}
