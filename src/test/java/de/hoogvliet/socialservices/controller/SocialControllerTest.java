package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.SocialServices;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class SocialControllerTest {
    private static final String RESPONSE_MSG = "There is a problem with the parameters you entered.";
    private static final String CATEGORY_NAME = "TEST-CATEGORY";
    private static final List<Category> ANY_CAT_LIST = Collections.emptyList();
    private static final List<Location> ANY_LOC_LIST = Collections.emptyList();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @MockBean
    private SocialServices socialServices;

    @AfterAll
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void categoriesEndpointIsAccessible() throws Exception {
        when(socialServices.getCategories()).thenReturn(ANY_CAT_LIST);
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(socialServices).getCategories();
    }

    @Test
    void categoriesEndpointIsAccessibleWithCityParameter() throws Exception {
        when(socialServices.getCategoriesForCity(anyInt())).thenReturn(ANY_CAT_LIST);
        mockMvc.perform(get("/categories?ct=33"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(socialServices).getCategoriesForCity(33);
    }

    @Test
    void socialEndpointIsAccessible() throws Exception {
        when(socialServices.getLocationsByCategory(anyInt())).thenReturn(ANY_LOC_LIST);
        mockMvc.perform(get("/social?c=17"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(socialServices).getLocationsByCategory(17);
    }

    @Test
    void socialEndpointWithoutParameterIsEmpty() throws Exception {
        mockMvc.perform(get("/social"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(socialServices, never()).getLocationsByCategory(17);
    }

    @Test
    void socialEndpointWithInvalidParameterShowErrorMessage() throws Exception {
        mockMvc.perform(get("/social?c=4rr5"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(RESPONSE_MSG));
    }

    @Test
    void socialEndpointIsAccessibleWithTwoParameters() throws Exception {
        when(socialServices.getLocationsByCategoryAndCity(anyInt(), anyInt())).thenReturn(ANY_LOC_LIST);
        mockMvc.perform(get("/social?c=17&ct=4"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(socialServices).getLocationsByCategoryAndCity(17, 4);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class CategoryEndpointTest {        
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
            Category c = categoryRepository.findByName(CATEGORY_NAME).get();
            assertNotNull(c);
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
    }
}