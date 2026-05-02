package de.hoogvliet.socialservices.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller 
public class ServiceErrorController {
    private static final String PATH = "/error";
    
    @GetMapping(value = PATH)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleError() {
        return new ModelAndView("error");
    }
}

