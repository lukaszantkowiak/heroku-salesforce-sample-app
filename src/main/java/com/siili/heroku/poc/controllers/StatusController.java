package com.siili.heroku.poc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @RequestMapping
    public String ok() {
        return "OK";
    }
}
