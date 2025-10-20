package com.etradeloop.controller;

import com.etradeloop.model.Item;
import com.etradeloop.model.User;
import com.etradeloop.repository.ItemRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepo;

    public ItemController(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    @PostMapping
    public Item addItem(@RequestBody Item item, @AuthenticationPrincipal User user) {
        item.setOwner(user);
        return itemRepo.save(item);
    }
}
