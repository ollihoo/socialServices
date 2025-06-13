package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class TSVCategoryParser {
    private static final int COLUMN_CATEGORIES = 6;
    private static final int NUMBER_OF_ELEMENTS = COLUMN_CATEGORIES + 1;
    private final CategoryRepository categoryRepository;

    public List<Category> getOrCreateCategories(String[] entry) {
        List<Category> categories = new ArrayList<>();
        if (entry.length >= NUMBER_OF_ELEMENTS) {
            categories.addAll(splitAndParseIntoCategoryList(entry));
        }
        return categories;
    }

    private ArrayList<Category> splitAndParseIntoCategoryList(String[] entry) {
        List<String> list = splitCategoriesEntry(entry);
        return list.stream()
                .map(this::searchCategoryByName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<String> splitCategoriesEntry(String[] entry) {
        if (entry.length < NUMBER_OF_ELEMENTS) {
            return Collections.emptyList();
        }
        String[] categories = entry[COLUMN_CATEGORIES].split(", ?");
        return Arrays.stream(categories).map(String::trim).distinct().filter(s -> ! s.isEmpty()).collect(Collectors.toList());
    }

    private Category searchCategoryByName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        return optionalCategory.orElseGet(() -> createCategory(categoryName));
    }

    private Category createCategory(String cat) {
        Category myCat = new Category();
        myCat.setName(cat);
        categoryRepository.save(myCat);
        return myCat;
    }

}
