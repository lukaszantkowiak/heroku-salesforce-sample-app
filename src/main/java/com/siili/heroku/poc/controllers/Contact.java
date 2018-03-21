package com.siili.heroku.poc.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

class Contact {
    private String firstName;
    private String lastName;
    private String privateEmail;

    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstname")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastname")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrivateEmail() {
        return privateEmail;
    }

    @JsonProperty("privateemail")
    public void setPrivateEmail(String privateEmail) {
        this.privateEmail = privateEmail;
    }
}
