package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return jdbcTemplate.queryForList("SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact");
    }

    @RequestMapping("/{id}")
    public List<Map<String, Object>> getContact(final int id) {
        return jdbcTemplate.queryForList("SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact WHERE id = ?", id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public int addContact(@RequestBody Contact contact) {
        jdbcTemplate.update(
                "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)",
                contact.getFirstName(), contact.getLastName(), contact.getPrivateEmail()
        );
        return -1;
//        return ResponseEntity.created()
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public int updateContact(@RequestBody Contact contact) {
        return jdbcTemplate.update(
                "UPDATE salesforce.Contact SET private_email__c = ? WHERE firstname = ? AND lastname = ?",
                contact.getPrivateEmail(), contact.getFirstName(), contact.getLastName()
        );
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK";
    }

    private static class Contact {
        private String firstName;
        private String lastName;
        private String privateEmail;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPrivateEmail() {
            return privateEmail;
        }

        public void setPrivateEmail(String privateEmail) {
            this.privateEmail = privateEmail;
        }
    }
}
