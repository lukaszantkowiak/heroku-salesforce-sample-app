package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/1")
    public String getContacts() {
        System.out.println("ContactsController.getContacts");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from salesforce.Contact");
        System.out.println("maps = " + maps);
        return "OK";
    }

    @RequestMapping("/2")
    public String ok() {
        return "OK";
    }
}
