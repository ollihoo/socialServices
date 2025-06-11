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
    private final CategoryRepository categoryRepository;

    public List<Category> getOrCreateCategories(String[] entry) {
        List<Category> categories = new ArrayList<>();
        if (entry.length >= 7) {
            categories.addAll(splitAndParseIntoCategoryList(entry));
        }
        return categories;
    }

    private ArrayList<Category> splitAndParseIntoCategoryList(String[] entry) {
        String[] categories = entry[COLUMN_CATEGORIES].split(", ?");
        List<String> list = Arrays.stream(categories).distinct().toList();
        return list.stream()
                .map(this::searchCategoryByName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Category searchCategoryByName(String categoryName) {
        String trimmedCategoryName = categoryName.trim();
        Optional<Category> optionalCategory = categoryRepository.findByName(trimmedCategoryName);
        return optionalCategory.orElseGet(() -> createCategory(trimmedCategoryName));
    }

    private Category createCategory(String cat) {
        Category myCat = new Category();
        myCat.setName(cat);
        categoryRepository.save(myCat);
        return myCat;
    }

}
