package com.parikhsit.bookmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Book Management API.
 *
 * @SpringBootApplication enables:
 *   - @Configuration      → Spring config source
 *   - @EnableAutoConfiguration → auto-configures beans
 *   - @ComponentScan      → scans com.parikhsit.bookmanagement and sub-packages
 */
@SpringBootApplication
public class BookManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookManagementApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Book Management API is up and running!");
        System.out.println("  Base URL : http://localhost:8080/api/v1");
        System.out.println("  Books URL: http://localhost:8080/api/v1/books");
        System.out.println("==============================================");
    }
}