package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.SocialServices;
import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;

@RestController@RequiredArgsConstructor
public class SocialController {
    private final SocialServices socialServices;

    @GetMapping("/social")
    @ResponseBody
    public List<Location> getSocialServiceEntities(
            @RequestParam(value = "c", required = false) Integer categoryId) {
        if (categoryId != null) {
            return socialServices.getLocationsByCategory(categoryId);
        }
        return Collections.emptyList();
    }
    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getCategories() {
        return socialServices.getCategories();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "The parameter you entered is invalid.";
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
