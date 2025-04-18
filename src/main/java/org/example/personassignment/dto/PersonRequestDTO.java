package org.example.personassignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PersonRequestDTO(
        @NotBlank(message = "Name must not be blank") String name,
        @NotNull(message = "Birth date must not be null") LocalDate birthDate,
        Long parent1Id,
        Long parent2Id,
        Long partnerId
) {}