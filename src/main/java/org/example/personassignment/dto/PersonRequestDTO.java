package org.example.personassignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PersonRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Long parent1Id;

    @NotNull
    private Long parent2Id;

    private Long partnerId;
}
