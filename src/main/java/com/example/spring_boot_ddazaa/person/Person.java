package com.example.spring_boot_ddazaa.person;

public record Person(
        int id,
        String name,
        int age,
        Gender gender,
        String email) {
}
