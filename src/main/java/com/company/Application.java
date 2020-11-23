package com.company;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Test application for Playtech. Commands between server(db) and client(server)
 * are made by http requests either using the postman or any other
 * application capable of making GET & POST http requests. Alternatively browser url tab can also be user.
 * All info concerning requests are located in com.company.controller.GameController
 *
 * @author  Ott Kask
 * NB!! server.port=8070 (application.properties)
 */

@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Skaneerib kogu projekti läbi, mis asub package'is ee.bcs.valiit ja võtab kõik klassid jne
        SpringApplication.run(Application.class, args);
    }
}
