package com.siili.heroku.poc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @RequestMapping("/status")
    public String greeting() {
        return "OK";
    }
}
