package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.SocialServices;
import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.City;
import de.hoogvliet.socialservices.socialservice.Location;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class SocialController {

    private final SocialServices socialServices;

    public SocialController(SocialServices socialServices) {
        this.socialServices = socialServices;
    }

    @RequestMapping(value = "/social", method = RequestMethod.GET)
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

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Category> getCategories() {
        return socialServices.getCategories();
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<City> getCities() {
        return socialServices.getCities();
    }
}