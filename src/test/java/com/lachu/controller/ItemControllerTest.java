package com.lachu.controller;

import com.lachu.exception.ResourceNotFoundException;
import com.lachu.entity.Item;
import com.lachu.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    // ---------- helpers ----------
    private Item sampleItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Keyboard");
        item.setDescription("Mechanical keyboard");
        item.setQuantity(10);
        item.setPrice(new BigDecimal("499.99"));
        return item;
    }

    private String json(String name, Object quantity, Object price) {
        return """
                {"name": %s, "description": "test item", "quantity": %s, "price": %s}
                """.formatted(name, quantity, price);
    }

    // ---------- POST /items ----------
    @Test
    @DisplayName("POST /items - valid item returns 201 and created item")
    void createItem_valid_returns201() throws Exception {
        when(itemService.createItem(any(Item.class))).thenReturn(sampleItem());

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Keyboard\"", 10, "499.99")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    @DisplayName("POST /items - blank name returns 400")
    void createItem_blankName_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"\"", 10, "499.99")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - name longer than 100 chars returns 400")
    void createItem_nameTooLong_returns400() throws Exception {
        String longName = "\"" + "a".repeat(101) + "\"";
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(longName, 10, "499.99")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - negative quantity returns 400")
    void createItem_negativeQuantity_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Keyboard\"", -5, "499.99")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - quantity 0 is allowed (assignment rule: quantity >= 0)")
    void createItem_zeroQuantity_returns201() throws Exception {
        Item item = sampleItem();
        item.setQuantity(0);
        when(itemService.createItem(any(Item.class))).thenReturn(item);

        // FAILS until you change @Min(1) to @Min(0) in Item.java
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Keyboard\"", 0, "499.99")))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /items - missing quantity returns 400")
    void createItem_missingQuantity_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Keyboard\", \"price\": 499.99}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - missing price returns 400")
    void createItem_missingPrice_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Keyboard\", \"quantity\": 5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - price with 3 decimal places returns 400")
    void createItem_priceTooManyDecimals_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Keyboard\"", 10, "10.123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /items - negative price returns 400")
    void createItem_negativePrice_returns400() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Keyboard\"", 10, "-1.00")))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET /items ----------
    @Test
    @DisplayName("GET /items - returns list of items")
    void getAllItems_returns200AndList() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Keyboard"));
    }

    @Test
    @DisplayName("GET /items - empty DB returns empty array")
    void getAllItems_empty_returnsEmptyArray() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of());

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- GET /items/pagination ----------
    @Test
    @DisplayName("GET /items/pagination - returns a page of items")
    void getAllItemsPaginated_returns200() throws Exception {
        when(itemService.getAllItems(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleItem())));

        mockMvc.perform(get("/items/pagination")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Keyboard"));
    }

    // ---------- GET /items/{id} ----------
    @Test
    @DisplayName("GET /items/{id} - existing id returns item")
    void getItemById_found_returns200() throws Exception {
        when(itemService.getItemById(1L)).thenReturn(sampleItem());

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    @Test
    @DisplayName("GET /items/{id} - unknown id returns 404")
    void getItemById_notFound_returns404() throws Exception {
        // needs a @RestControllerAdvice mapping ResourceNotFoundException -> 404
        when(itemService.getItemById(99L)).thenThrow(new ResourceNotFoundException("Item not found"));

        mockMvc.perform(get("/items/99"))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /items/{id} ----------
    @Test
    @DisplayName("PUT /items/{id} - valid update returns 200")
    void updateItem_valid_returns200() throws Exception {
        Item updated = sampleItem();
        updated.setName("Gaming Keyboard");
        when(itemService.updateItemById(eq(1L), any(Item.class))).thenReturn(updated);

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"Gaming Keyboard\"", 10, "499.99")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Keyboard"));
    }

    @Test
    @DisplayName("PUT /items/{id} - invalid body returns 400")
    void updateItem_invalid_returns400() throws Exception {
        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("\"\"", -1, "499.99")))
                .andExpect(status().isBadRequest());
    }

    // ---------- DELETE /items/{id} ----------
    @Test
    @DisplayName("DELETE /items/{id} - returns 204 and calls service")
    void deleteItem_returns204() throws Exception {
        doNothing().when(itemService).deleteItemById(1L);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());

        verify(itemService).deleteItemById(1L);
    }
}