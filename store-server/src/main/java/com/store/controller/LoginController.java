package com.store.controller;

import com.core.web.WebContext;
import com.privilege.security.Principal;
import com.privilege.security.SecurityContext;
import com.store.system.StoreConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 */
@RequestMapping
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("version", StoreConfig.gVersion);
        Principal principal = (Principal) WebContext.getRequest().getSession().getAttribute(SecurityContext.PRINCIPAL);
        if (principal != null && principal.isAuthentication()) {
            return "redirect:/home";
        }
        return "/login";
    }

}
