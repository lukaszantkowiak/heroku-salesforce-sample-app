package com.siili.heroku.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final String port = System.getenv().get("PORT");
        final String dyno = System.getenv().get("DYNO");
        if (port != null && dyno != null) {
            System.getProperties().put("server.port", port);
        }

        SpringApplication.run(Application.class, args);
    }
}
