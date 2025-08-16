package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.CategoryRepository;
import de.hoogvliet.socialservices.socialservice.City;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.SocialServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController@RequiredArgsConstructor @Slf4j
public class SocialController {
    private final SocialServices socialServices;
    private final CategoryRepository categoryRepository;

    @GetMapping("/social")
    @ResponseBody
    public List<Location> getSocialServiceEntities(
            @RequestParam(value = "c", required = false) Integer categoryId,
            @RequestParam(value="ct", required = false) Integer cityId) {
        if (categoryId != null) {
            if (cityId != null) {
                return socialServices.getLocationsByCategoryAndCity(categoryId, cityId);
            }
            return socialServices.getLocationsByCategory(categoryId);
        }
        return Collections.emptyList();
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getCategories(
            @RequestParam(value="ct", required = false) Integer cityId) {
        if (cityId != null ) {
            return socialServices.getCategoriesForCity(cityId);
        }
        return socialServices.getCategories();
    }

    @GetMapping(value = "/cities")
    @ResponseBody
    public List<City> getCities() {
        return socialServices.getCities();
    }

    @Operation(summary = "Create a new category", description = "Creates a new category", tags = "Category-CRUD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created - Category created"),
        @ApiResponse(responseCode = "409", description = "Conflict - Category already exists"),
    })
    @PostMapping("/category")
    @ResponseBody
    public ResponseEntity<Category> createCategory(@RequestBody String categoryName) {
        String cleanName = categoryName
            .replace("\"", "")
            .replace(" und ", " & ")
            .replaceAll(" \\s+", " ")
            .strip();
        try {
            Category existingCategory = categoryRepository.findByName(cleanName).get();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existingCategory);
        } catch(NoSuchElementException e) {
            Category category = new Category();
            category.setName(cleanName);
            Category newCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
        }
    }

    @Operation(summary = "Read a category by ID", description = "Retrieves a category by its ID", tags = "Category-CRUD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - Category retrieved"),
        @ApiResponse(responseCode = "404", description = "Not Found - Category ID not found", content = @Content(
            schema = @Schema(implementation = Void.class)
        )),
    })
    @GetMapping("/category/{id}")
    @ResponseBody
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        try {
            Category category = categoryRepository.findById(id).get();
            return ResponseEntity.ok(category);
        } catch(NoSuchElementException e) {
            log.warn("Category not found: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Read all categories", description = "Retrieves all categories", tags = "Category-CRUD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - Categories retrieved")
    })
    @GetMapping("/category")
    @ResponseBody
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(socialServices.getCategories());
    }

    @Operation(summary = "Update an existing category", description = "Updates the name of an existing category", tags = "Category-CRUD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - Category updated"),
        @ApiResponse(responseCode = "404", description = "Not Found - Category ID not found", content = @Content(
            schema = @Schema(implementation = Void.class)
        ))
    })
    @PutMapping("/category")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        int catId = category.getId();
        try{
            categoryRepository.findById(catId).get();
            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(updatedCategory);
        } catch(NoSuchElementException e) {
            log.warn("Category not found: " + catId);
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Delete a category by ID", description = "Deletes a category by ID", tags = "Category-CRUD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content - Category deleted"),
        @ApiResponse(responseCode = "404", description = "Not Found - Category ID not found")
    })
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        //TODO: Implement the logic to delete a category by ID
        log.error("DELETE `category` not yet implemented");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(
                "There is a problem with the parameters you entered.",
                HttpStatus.BAD_REQUEST);
    }
}
