package com.ninegold.ninegoldapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping({"/login", "/register", "/forgot-password", "/faq", "/profile"})
    public String index() {
        return "forward:/index.html";
    }
}