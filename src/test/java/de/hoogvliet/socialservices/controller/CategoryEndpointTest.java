package de.hoogvliet.socialservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class CategoryEndpointTest {
    private static final String CATEGORY_NAME = "TEST-CATEGORY";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterAll
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createsWithPost() throws Exception {
        mockMvc.perform(post("/category")
                        .content(CATEGORY_NAME))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(CATEGORY_NAME)));
    }

    @Test
    @Order(2)
    void createsWithPostConflict() throws Exception {
        mockMvc.perform(post("/category")
                        .content(CATEGORY_NAME))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.name", Matchers.is(CATEGORY_NAME)));
    }

    @Test
    @Order(3)
    void readsWithGet() throws Exception {
        Category c = getTestCategory();
        int id = c.getId();
        mockMvc.perform(get("/category/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(CATEGORY_NAME)));
    }

    @Test
    @Order(4)
    void readsWithGetNotFound() throws Exception {
        int nonExistentId = -1;
        mockMvc.perform(get("/category/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void updatesWithPut() throws Exception {
        Category c = getTestCategory();
        String newName = "UpdatedCategoryName";
        c.setName(newName);
        mockMvc.perform(put("/category")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(c))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(newName)));
        c.setName(CATEGORY_NAME);
        mockMvc.perform(put("/category")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(c))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(CATEGORY_NAME)));
    }

    private Category getTestCategory() {
        Optional<Category> c = categoryRepository.findByName(CATEGORY_NAME);
        assertTrue(c.isPresent());
        return c.get();
    }

    @Test
    @Order(6)
    void deletesWithDelete() throws Exception {
        Category c = categoryRepository.findByName(CATEGORY_NAME).get();
        assertNotNull(c);
        int id = c.getId();
        mockMvc.perform(delete("/category/" + id))
                .andExpect(status().isNoContent());
        assertTrue(categoryRepository.findByName(CATEGORY_NAME).isEmpty());
    }

    @Test
    @Order(7)
    void deletesWithDeleteNotFound() throws Exception {
        int invalidId = -1;
        mockMvc.perform(delete("/category/" + invalidId))
                .andExpect(status().isNotFound());
    }

}
