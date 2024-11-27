package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final List<Category> ANY_CATEGORIES_LIST = Collections.emptyList();
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks private CategoryService categoryService;

    @Test
    void getAllCategoriesUsesRepositoryAndReturnsResult() {
        when(categoryRepository.findAll()).thenReturn(ANY_CATEGORIES_LIST);
        List<Category> allCategories = categoryService.getAllCategories();
        assertEquals(ANY_CATEGORIES_LIST, allCategories);
    }
}