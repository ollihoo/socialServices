package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.SocialServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CategoryControllerTest {
    private static final List<Category> ANY_CAT_LIST = Collections.emptyList();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SocialServices socialServices;

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
}