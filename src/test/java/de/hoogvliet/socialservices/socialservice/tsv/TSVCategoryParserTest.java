package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import de.hoogvliet.socialservices.socialservice.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TSVCategoryParserTest {
    public static final String ESSEN = "Essen";
    @InjectMocks
    private TSVCategoryParser tsvCategoryParser;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @Test
    void testThatAllGivenCategoriesAreReturned() {
        String[] inputArray = { "", "", "", "", "", "", "Essen, Trinken, Weinkarte"};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals(3, actualCategories.size());
    }

    @Test
    void testThatGivenCategoryIsFound() {
        String[] inputArray = { "", "", "", "", "", "", ESSEN};
        when(categoryService.createCategory(anyString())).thenReturn(createCategory());
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals("Essen", actualCategories.getFirst().getName());
        verify(categoryService).createCategory(ESSEN);
    }

    @Test
    void testThatGivenCategoryIsTrimmedCorrectlyFound() {
        String[] inputArray = { "", "", "", "", "", "","\tEssen  "};
        when(categoryService.createCategory(anyString())).thenReturn(createCategory());
        tsvCategoryParser.getOrCreateCategories(inputArray);
        verify(categoryService).createCategory("Essen");
    }

    @Test
    void testThatCategoryDuplicationIsReduced() {
        String[] inputArray = { "", "", "", "", "", "","Essen, Essen"};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals(1, actualCategories.size());
    }

    @Test
    void testThatCategoryIsCheckedAgainstDatabase() {
        String[] inputArray = { "", "", "", "", "", "",ESSEN};
        tsvCategoryParser.getOrCreateCategories(inputArray);
        verify(categoryRepository).findByName("Essen");
    }

    @Test
    void splitCategoriesEntry_splits_string_into_categories () {
        String[] inputArray = { "", "", "", "", "", "",ESSEN};
        List<String> result = TSVCategoryParser.splitCategoriesEntry(inputArray);
        assertEquals(ESSEN, result.getFirst());
        assertEquals(1, result.size());
    }

    @Test
    void splitCategoriesEntry_can_handle_whitespace_string_correctly () {
        String[] inputArray = { "", "", "", "", "", ""," "};
        List<String> result = TSVCategoryParser.splitCategoriesEntry(inputArray);
        assertTrue(result.isEmpty());
    }

    @Test
    void splitCategoriesEntry_can_handle_shorter_input_arrays () {
        String[] inputArray = { "", "", "", "", "", "" };
        List<String> result = TSVCategoryParser.splitCategoriesEntry(inputArray);
        assertTrue(result.isEmpty());
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName(ESSEN);
        return category;
    }

}