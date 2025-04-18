package org.example.personassignment.mapper;

import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.example.personassignment.repository.PersonRepository;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    private final PersonRepository personRepository;

    public PersonMapper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person toEntity(PersonRequestDTO personRequestDTO) {
        Person person = new Person();
        person.setName(personRequestDTO.name());
        person.setBirthDate(personRequestDTO.birthDate());

        setRelationships(personRequestDTO, person);

        return person;
    }

    public PersonResponseDTO toDto(Person person) {
        return new PersonResponseDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                person.getParent1() != null ? person.getParent1().getId() : null,
                person.getParent2() != null ? person.getParent2().getId() : null,
                person.getPartner() != null ? person.getPartner().getId() : null
        );
    }

    public void updateEntity(PersonRequestDTO personRequestDTO, Person person) {
        person.setName(personRequestDTO.name());
        person.setBirthDate(personRequestDTO.birthDate());

        setRelationships(personRequestDTO, person);
    }

    private void setRelationships(PersonRequestDTO personRequestDTO, Person person) {
        person.setParent1(findParent(personRequestDTO.parent1Id(), Person.EMPTY_PARENT_1_ID, "Parent1"));
        person.setParent2(findParent(personRequestDTO.parent2Id(), Person.EMPTY_PARENT_2_ID, "Parent2"));
        person.setPartner(findPartner(personRequestDTO.partnerId()));
    }

    private Person findParent(Long parentId, Long emptyParentId, String parentType) {
        return parentId != null
                ? personRepository.findById(parentId).orElseThrow(() -> new RuntimeException(parentType + " not found"))
                : personRepository.findById(emptyParentId).orElseThrow(() -> new RuntimeException("Empty " + parentType + " not found"));
    }

    private Person findPartner(Long partnerId) {
        return partnerId != null
                ? personRepository.findById(partnerId).orElseThrow(() -> new RuntimeException("Partner not found"))
                : null;
    }
}