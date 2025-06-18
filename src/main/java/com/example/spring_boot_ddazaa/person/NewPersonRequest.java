package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.validation.Foo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewPersonRequest(
        @NotEmpty String name,
        @Min(16) int age,
        @NotNull Gender gender) {
}
