package ru.taf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LibrarianApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibrarianApplication.class, args);
    }
}
