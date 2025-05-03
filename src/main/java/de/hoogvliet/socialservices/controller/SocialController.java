package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.City;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.SocialServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;

@RestController@RequiredArgsConstructor @Slf4j
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

    @RequestMapping(value = "/social/city", method=RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Location> getSocialServiceCityEntities(
            @RequestParam(value = "ct", required = false) String cityId) {
        if (cityId != null) {
            int citId;
            try {
                citId = Integer.parseInt(cityId);
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
            return socialServices.getLocationsByCity(citId);
        }

        return Collections.emptyList();
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getCategories() {
        return socialServices.getCategories();
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<City> getCities() {
        return socialServices.getCities();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn(ex.getMessage());
        String error = "The parameter you entered is invalid.";
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
