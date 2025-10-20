package com.etradeloop.repository;

import com.etradeloop.model.Item;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Basic CRUD provided by JpaRepository (save, findById, findAll, delete, etc.)

    // Optional: Custom method to find items by owner (if needed for filtering)
    List<Item> findByOwnerId(Long ownerId);
}