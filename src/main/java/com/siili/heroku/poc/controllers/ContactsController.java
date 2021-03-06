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

import java.util.List;
import java.util.Map;

import static com.siili.heroku.poc.controllers.ContactsController.CONTROLLER_PATH;

@RestController
@RequestMapping(CONTROLLER_PATH)
public class ContactsController {
    static final String CONTROLLER_PATH = "/contacts";

    private static final String GET_CONTACTS_QUERY = "SELECT id, firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact";
    private static final String GET_CONTACT_QUERY = "SELECT id, firstname, lastname, private_email__c AS privateemail FROM salesforce.Contact WHERE id = ?";
    private static final String INSERT_CONTACT_QUERY = "INSERT INTO salesforce.Contact (firstname, lastname, private_email__c) VALUES (?, ?, ?)";
    private static final String UPDATE_CONTACT_QUERY = "UPDATE salesforce.Contact SET private_email__c = ? WHERE firstname = ? AND lastname = ?";
    private static final String REMOVE_CONTACT_QUERY = "DELETE FROM salesforce.Contact WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping()
    public ResponseEntity<List<Map<String, Object>>> getContacts() {
        return ResponseEntity.ok(jdbcTemplate.queryForList(GET_CONTACTS_QUERY));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getContact(@PathVariable int id) {
        List<Map<String, Object>> contact = jdbcTemplate.queryForList(GET_CONTACT_QUERY, id);
        if (contact.size() == 1) {
            return ResponseEntity.ok(contact.get(0));
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> addContact(@RequestBody Contact contact) {
        jdbcTemplate.update(
                INSERT_CONTACT_QUERY,
                contact.getFirstName(), contact.getLastName(), contact.getPrivateEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> updateContact(@RequestBody Contact contact) {
        jdbcTemplate.update(
                UPDATE_CONTACT_QUERY,
                contact.getPrivateEmail(), contact.getFirstName(), contact.getLastName()
        );

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeContact(@PathVariable int id) {
        jdbcTemplate.update(
                REMOVE_CONTACT_QUERY,
                id
        );

        return ResponseEntity.ok().build();
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK";
    }

}
