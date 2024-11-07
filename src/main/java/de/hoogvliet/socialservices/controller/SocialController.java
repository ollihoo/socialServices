package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.SocialServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocialController {

    private final SocialServices socialServices;

    public SocialController(SocialServices socialServices) {
        this.socialServices = socialServices;
    }

    @RequestMapping(value = "/social", method = RequestMethod.GET)
    public String getSocialServiceEntities() {
        socialServices.getAllEntries();
        return "SOCIAL2";
    }
}
