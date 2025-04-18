package org.example.personassignment.mapper;

import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.example.personassignment.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PersonMapper {
    private final PersonRepository personRepository;

    public PersonMapper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person toEntity(PersonRequestDTO personRequestDTO) {
        Person parent1 = personRequestDTO.parent1Id() != null
                ? personRepository.findById(personRequestDTO.parent1Id()).orElseThrow(() -> new RuntimeException("Parent1 not found"))
                : personRepository.findById(Person.EMPTY_PARENT_1_ID).orElseThrow(() -> new RuntimeException("Empty Parent1 not found"));

        Person parent2 = personRequestDTO.parent2Id() != null
                ? personRepository.findById(personRequestDTO.parent2Id()).orElseThrow(() -> new RuntimeException("Parent2 not found"))
                : personRepository.findById(Person.EMPTY_PARENT_2_ID).orElseThrow(() -> new RuntimeException("Empty Parent2 not found"));

        Person partner = personRequestDTO.partnerId() != null
                ? personRepository.findById(personRequestDTO.partnerId()).orElseThrow(() -> new RuntimeException("Partner not found"))
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
                person.getParent1() != null ? person.getParent1().getId() : null,
                person.getParent2() != null ? person.getParent2().getId() : null,
                person.getPartner() != null ? person.getPartner().getId() : null,
                person.getChildren().stream().map(Person::getId).collect(Collectors.toList())
        );
    }
}
