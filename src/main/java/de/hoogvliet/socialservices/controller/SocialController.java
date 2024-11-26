package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.SocialServices;
import de.hoogvliet.socialservices.socialservice.Category;
import de.hoogvliet.socialservices.socialservice.Location;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SocialController {

    private final SocialServices socialServices;

    public SocialController(SocialServices socialServices) {
        this.socialServices = socialServices;
    }

    @RequestMapping(value = "/social", method = RequestMethod.GET)
    @ResponseBody
    public List<Location> getSocialServiceEntities() {
        return socialServices.getAllEntries();
    }
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public List<Category> getCategories() {
        return socialServices.getCategories();
    }
}
