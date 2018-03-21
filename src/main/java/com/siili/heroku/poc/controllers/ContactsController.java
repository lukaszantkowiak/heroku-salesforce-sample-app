package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.siili.heroku.poc.controllers.ContactsController.CONTROLLER_PATH;

@RestController
@RequestMapping(CONTROLLER_PATH)
public class ContactsController {
    static final String CONTROLLER_PATH = "/contacts";

    private static final String GET_CONTACTS_QUERY = "SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact";
    private static final String GET_CONTACT_QUERY = "SELECT firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact WHERE id = ?";
    private static final String INSERT_CONTACT_QUERY = "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)";
    private static final String UPDATE_CONTACT_QUERY = "UPDATE salesforce.Contact SET private_email__c = ? WHERE firstname = ? AND lastname = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping()
    public List<Map<String, Object>> getContacts() {
        return jdbcTemplate.queryForList(GET_CONTACTS_QUERY);
    }

    @RequestMapping("/{id}")
    public List<Map<String, Object>> getContact(@PathVariable int id) {
        return jdbcTemplate.queryForList(GET_CONTACT_QUERY, id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<URI> addContact(@RequestBody Contact contact) {
        jdbcTemplate.update(
                INSERT_CONTACT_QUERY,
                contact.getFirstName(), contact.getLastName(), contact.getPrivateEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<URI> updateContact(@RequestBody Contact contact) {
        jdbcTemplate.update(
                UPDATE_CONTACT_QUERY,
                contact.getPrivateEmail(), contact.getFirstName(), contact.getLastName()
        );

        return ResponseEntity.ok().build();
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK";
    }

}
