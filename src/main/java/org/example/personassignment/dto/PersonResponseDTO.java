package org.example.personassignment.dto;

import java.time.LocalDate;

public record PersonResponseDTO(Long id, String name, LocalDate birthDate, Long parent1Id, Long parent2Id,
                                Long partnerId) {
}
