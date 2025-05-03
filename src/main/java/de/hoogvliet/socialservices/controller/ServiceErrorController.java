package de.hoogvliet.socialservices.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@Controller 
public class ServiceErrorController implements ErrorController {
    private static final String PATH = "/error";
    
    @RequestMapping(value = PATH, method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleError() {
        ModelAndView vm = new ModelAndView("error");
        return vm;
    }
}

