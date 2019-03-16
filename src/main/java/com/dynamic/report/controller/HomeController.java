package com.dynamic.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController {

    @RequestMapping("")
    public String homeIndex() {
        return "main";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("test")
    public String test() {
        return "test";
    }

}
