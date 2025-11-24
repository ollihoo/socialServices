package de.hoogvliet.socialservices.socialservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(String cat) {
        Category myCat = new Category();
        myCat.setName(cat);
        categoryRepository.save(myCat);
        return myCat;
    }
}
