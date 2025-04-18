package org.example.personassignment.mapper;

import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonMapper {
    public PersonResponseDTO toDto(Person person) {
        List<Long> childrenIds = person.getChildren().stream()
                .map(Person::getId)
                .collect(Collectors.toList());

        return new PersonResponseDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                person.getParent1() != null ? person.getParent1().getId() : null,
                person.getParent2() != null ? person.getParent2().getId() : null,
                person.getPartner() != null ? person.getPartner().getId() : null,
                childrenIds
        );
    }

}
