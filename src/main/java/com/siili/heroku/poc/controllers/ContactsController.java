package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactsController {
    private static final String GET_CONTACTS_QUERY = "SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact";
    private static final String GET_CONTACT_QUERY = "SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact WHERE id = ?";
    private static final String INSERT_CONTACT_QUERY = "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)";
    private static final String UPDATE_CONTACT_QUERY = "UPDATE salesforce.Contact SET private_email__c = ? WHERE firstname = ? AND lastname = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/")
    public List<Map<String, Object>> getContacts() {
        return jdbcTemplate.queryForList(GET_CONTACTS_QUERY);
    }

    @RequestMapping("/{id}")
    public List<Map<String, Object>> getContact(@PathVariable int id) {
        return jdbcTemplate.queryForList(GET_CONTACT_QUERY, id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public int addContact(@RequestBody Contact contact) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_CONTACT_QUERY, new String[] {"id"});
                    ps.setString(1, contact.getFirstName());
                    ps.setString(2, contact.getLastName());
                    ps.setString(3, contact.getPrivateEmail());
                    return ps;
                },
                keyHolder);
//        jdbcTemplate.update(
//                "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)",
//                contact.getFirstName(), contact.getLastName(), contact.getPrivateEmail()
//        );
        return keyHolder.getKey().intValue();
//        return ResponseEntity.created()
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public int updateContact(@RequestBody Contact contact) {
        return jdbcTemplate.update(
                UPDATE_CONTACT_QUERY,
                contact.getPrivateEmail(), contact.getFirstName(), contact.getLastName()
        );
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK " + System.getenv().get("DYNO");
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
