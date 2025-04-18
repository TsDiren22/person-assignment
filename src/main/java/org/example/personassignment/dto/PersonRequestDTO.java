package org.example.personassignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PersonRequestDTO(
        @NotBlank(message = "Name must not be blank") String name,
        @NotNull(message = "Birth date must not be null") LocalDate birthDate,
        @NotNull(message = "Parent 1 ID must not be null") Long parent1Id,
        @NotNull(message = "Parent 2 ID must not be null") Long parent2Id,
        Long partnerId
) {}