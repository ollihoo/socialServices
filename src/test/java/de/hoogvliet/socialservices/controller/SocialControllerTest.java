package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.SocialServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SocialControllerTest {
    private static final String RESPONSE_MSG = "There is a problem with the parameters you entered.";
    private static final List<Category> ANY_CAT_LIST = Collections.emptyList();
    private static final List<Location> ANY_LOC_LIST = Collections.emptyList();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
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


}