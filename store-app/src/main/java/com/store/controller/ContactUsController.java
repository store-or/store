package com.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by laizhiyang on 2017/6/17.
 */
@Controller
@RequestMapping("/contact")
public class ContactUsController extends BaseController {

    @RequestMapping("/index")
    public String contact() {
        return "/store/contact/index";
    }
}
