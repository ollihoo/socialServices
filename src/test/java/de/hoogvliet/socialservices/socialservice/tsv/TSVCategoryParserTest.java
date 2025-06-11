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
}