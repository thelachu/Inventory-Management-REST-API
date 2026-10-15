package com.lachu.controller;

import com.lachu.config.SecurityConfig;
import com.lachu.security.ApiKeyFilter;
import com.lachu.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import({SecurityConfig.class, ApiKeyFilter.class})
@TestPropertySource(properties = "inventory.api-key=test-key")
class ApiKeySecurityTest {

    private static final String HEADER = "X-API-KEY";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Test
    @DisplayName("No API key header -> 401")
    void noApiKey_returns401() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid API Key"));
    }

    @Test
    @DisplayName("Wrong API key -> 401")
    void wrongApiKey_returns401() throws Exception {
        mockMvc.perform(get("/items").header(HEADER, "wrong-key"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Correct API key -> 200")
    void correctApiKey_returns200() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of());

        mockMvc.perform(get("/items").header(HEADER, "test-key"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Wrong API key on DELETE -> 401 (all endpoints are protected)")
    void deleteWithoutKey_returns401() throws Exception {
        mockMvc.perform(delete("/items/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Correct API key on DELETE -> 204")
    void deleteWithKey_returns204() throws Exception {
        mockMvc.perform(delete("/items/1").header(HEADER, "test-key"))
                .andExpect(status().isNoContent());
    }
}