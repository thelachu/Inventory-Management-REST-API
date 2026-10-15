package com.lachu.service;

import com.lachu.entity.Item;
import com.lachu.exception.ResourceNotFoundException;
import com.lachu.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;

    //To Create a new Item into DB
    @Override
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    //To Get all Items from DB
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    //To Get all Items from DB by Pagination
    @Override
    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    //To Get Item by ID from DB
    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no item found by this id: " + id));
    }

    //To Update Item in DB
    @Override
    public Item updateItemById(Long id, Item item) {
        Item updatedItem = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no item found by this id: " + id));
        updatedItem.setName(item.getName());
        updatedItem.setDescription(item.getDescription());
        updatedItem.setQuantity(item.getQuantity());
        updatedItem.setPrice(item.getPrice());
        return itemRepository.save(updatedItem);
    }

    //To Delete Item in DB by ID
    @Override
    public void deleteItemById(Long id) {
        itemRepository.delete(itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no item found by this id: " + id)));
    }

}
