package com.etradeloop.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Item {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    private User owner;

    // Default constructor (required by JPA)
    public Item() {
    }

    // Parameterized constructor
    public Item(Long id, String name, String description, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setOwner(User owner) { this.owner = owner; }
}
