package com.lachu.controller;

import com.lachu.entity.Item;
import com.lachu.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    //To Create new Item into DB
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    //To Get all Items from DB
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    //To Get all Items from DB by Pagination
    @GetMapping("/pagination")
    public ResponseEntity<Page<Item>> getAllItems(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Item> items = itemService.getAllItems(pageable);
        return ResponseEntity.ok(items);
    }

    //To Get Item by ID from DB
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    //To Update Item in DB
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItemById(@PathVariable("id") Long id, @Valid @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItemById(id, item));
    }

    //To Delete Item in DB by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable(name = "id") Long id) {
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build();
    }

}
