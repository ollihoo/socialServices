package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.SocialServices;
import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController@RequiredArgsConstructor
public class SocialController {
    private final SocialServices socialServices;

    @GetMapping("/social")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Location> getSocialServiceEntities(
            @RequestParam(value = "c", required = false) String categoryId) {
        if (categoryId != null) {
            int catId;
            try {
                catId = Integer.parseInt(categoryId);
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
            return socialServices.getLocationsByCategory(catId);
        }
        return Collections.emptyList();
    }
    @GetMapping("/categories")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Category> getCategories() {
        return socialServices.getCategories();
    }
}
