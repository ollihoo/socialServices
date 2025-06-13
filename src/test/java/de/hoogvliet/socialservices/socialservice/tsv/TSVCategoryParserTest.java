package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TSVCategoryParserTest {
    @InjectMocks
    private TSVCategoryParser tsvCategoryParser;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testThatAllGivenCategoriesAreReturned() {
        String[] inputArray = { "", "", "", "", "", "", "Essen, Trinken, Weinkarte"};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals(3, actualCategories.size());
    }

    @Test
    void testThatGivenCategoryIsFound() {
        String[] inputArray = { "", "", "", "", "", "", "Essen"};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals("Essen", actualCategories.getFirst().getName());
    }

    @Test
    void testThatGivenCategoryIsTrimmedCorrectlyFound() {
        String[] inputArray = { "", "", "", "", "", "","\tEssen  "};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals("Essen", actualCategories.getFirst().getName());
    }

    @Test
    void testThatCategoryDuplicationIsReduced() {
        String[] inputArray = { "", "", "", "", "", "","Essen, Essen"};
        List<Category> actualCategories = tsvCategoryParser.getOrCreateCategories(inputArray);
        assertEquals(1, actualCategories.size());
    }

    @Test
    void testThatCategoryIsCheckedAgainstDatabase() {
        String[] inputArray = { "", "", "", "", "", "","Essen"};
        tsvCategoryParser.getOrCreateCategories(inputArray);
        verify(categoryRepository).findByName("Essen");
    }

    @Test
    void splitCategoriesEntry_splits_string_into_categories () {
        String[] inputArray = { "", "", "", "", "", "","Essen"};
        List<String> result = TSVCategoryParser.splitCategoriesEntry(inputArray);
        assertEquals("Essen", result.get(0));
        assertTrue(result.size() == 1);
    }

    @Test
    void splitCategoriesEntry_can_handle_whitespace_string_correctly () {
        String[] inputArray = { "", "", "", "", "", ""," "};
        List<String> result = TSVCategoryParser.splitCategoriesEntry(inputArray);
        assertTrue(result.isEmpty());
    }

}