package com.lachu.service;


import com.lachu.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);

    List<Item> getAllItems();

    Page<Item> getAllItems(Pageable pageable);

    Item getItemById(Long id);

    Item updateItemById(Long id, Item item);

    void deleteItemById(Long id);
}
