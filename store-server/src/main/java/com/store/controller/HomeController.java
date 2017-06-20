package com.store.controller;

import com.core.controller.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 */
@Controller
public class HomeController extends AbstractController {

    @RequestMapping("/")
    public String index() {
        return "forward:/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "/home";
    }
    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "/unauthorized";
    }
}
