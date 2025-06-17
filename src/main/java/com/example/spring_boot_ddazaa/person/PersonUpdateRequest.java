package com.example.spring_boot_ddazaa.person;

public record PersonUpdateRequest(
        String name,
        Integer age
) {
}