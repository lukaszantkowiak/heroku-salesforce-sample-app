package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/")
    public List<Map<String, Object>> getContacts() {
        System.out.println("ContactsController.getContacts");
        return jdbcTemplate.queryForList("select firstname, lastname, private_email__c from salesforce.Contact");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public int addContact(@RequestBody String firstName, @RequestBody String lastName, @RequestBody String privateEmail) {
        int update = jdbcTemplate.update(
                "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)",
                firstName, lastName, privateEmail
        );
        return update;
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK";
    }
}
