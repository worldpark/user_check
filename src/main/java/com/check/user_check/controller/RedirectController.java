package com.check.user_check.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/admin")
    public String adminForward(){
        return "forward:/index.html";
    }

    @GetMapping("/admin/**")
    public String adminForwardSubPaths(){
        return "forward:/index.html";
    }

    @GetMapping("/user")
    public String userForward(){
        return "forward:/index.html";
    }

    @GetMapping("/user/**")
    public String userForwardSubPaths(){
        return "forward:/index.html";
    }
}
