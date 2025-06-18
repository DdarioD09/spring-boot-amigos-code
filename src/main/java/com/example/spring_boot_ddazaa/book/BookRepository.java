package com.example.spring_boot_ddazaa.book;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    public List<Book> selectAllBooks() {
        return List.of(
                new Book(1, "Amigos Code", "Dario Daza"),
                new Book(2, "Java projects", "Jorge Ramirez"),
                new Book(3, "Spring Boot", "Spring Boot Daza")
        );
    }
}
