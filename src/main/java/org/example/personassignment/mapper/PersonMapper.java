package org.example.personassignment.mapper;

import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PersonMapper {
    public Person toEntity(PersonRequestDTO personRequestDTO) {
        Person parent1 = personRepository.findById(personRequestDTO.parent1Id())
                .orElseThrow(() -> new RuntimeException("Parent 1 not found"));
        Person parent2 = personRepository.findById(personRequestDTO.parent2Id())
                .orElseThrow(() -> new RuntimeException("Parent 2 not found"));
        Person partner = personRequestDTO.partnerId() != null
                ? personRepository.findById(personRequestDTO.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found"))
                : null;

        Person person = new Person();
        person.setName(personRequestDTO.name());
        person.setBirthDate(personRequestDTO.birthDate());
        person.setParent1(parent1);
        person.setParent2(parent2);
        person.setPartner(partner);
        return person;
    }

    public PersonResponseDTO toDto(Person person) {
        return new PersonResponseDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                person.getParent1().getId(),
                person.getParent2().getId(),
                person.getPartner() != null ? person.getPartner().getId() : null,
                person.getChildren().stream().map(Person::getId).collect(Collectors.toList())
        );
    }

}
