package com.siili.heroku.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.PreparedStatement;
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

    @RequestMapping("/")
    public List<Map<String, Object>> getContacts() {
        return jdbcTemplate.queryForList(GET_CONTACTS_QUERY);
    }

    @RequestMapping("/{id}")
    public List<Map<String, Object>> getContact(@PathVariable int id) {
        return jdbcTemplate.queryForList(GET_CONTACT_QUERY, id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<URI> addContact(@RequestBody Contact contact, UriComponentsBuilder uriComponentsBuilder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_CONTACT_QUERY, new String[]{"id"});
                    ps.setString(1, contact.getFirstName());
                    ps.setString(2, contact.getLastName());
                    ps.setString(3, contact.getPrivateEmail());
                    return ps;
                },
                keyHolder);

        UriComponents uriComponents = uriComponentsBuilder.path(CONTROLLER_PATH + "/{id}").buildAndExpand(keyHolder.getKey().intValue());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<URI> updateContact(@RequestBody Contact contact, UriComponentsBuilder uriComponentsBuilder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_CONTACT_QUERY, new String[]{"id"});
                    ps.setString(1, contact.getPrivateEmail());
                    ps.setString(2, contact.getFirstName());
                    ps.setString(3, contact.getLastName());
                    return ps;
                },
                keyHolder);

        UriComponents uriComponents = uriComponentsBuilder.path(CONTROLLER_PATH + "/{id}").buildAndExpand(keyHolder.getKey().intValue());
        return ResponseEntity.ok(uriComponents.toUri());
    }

    @RequestMapping("/status")
    public String ok() {
        return "OK";
    }

}
