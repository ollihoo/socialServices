package de.hoogvliet.socialservices.socialservice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private static final int COLUMN_CATEGORIES = 6;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getOrCreateCategories(String[] entry) {
        List<Category> categorySet = new ArrayList<>();
        if (entry.length >= 7) {
            String[] categories = entry[COLUMN_CATEGORIES].split(", ?");
            Arrays.stream(categories).forEach((String cat) -> {
                Optional<Category> optionalCategory =categoryRepository.findByName(cat);
                if (optionalCategory.isPresent()) {
                    categorySet.add(optionalCategory.get());
                } else {
                    categorySet.add(createCategory(cat));
                }
            });
        }
        return categorySet;
    }

    private Category createCategory(String cat) {
        Category myCat = new Category();
        myCat.setName(cat);
        categoryRepository.save(myCat);
        return myCat;
    }

}
