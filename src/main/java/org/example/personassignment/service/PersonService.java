package org.example.personassignment.service;

import jakarta.transaction.Transactional;
import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.example.personassignment.mapper.PersonMapper;
import org.example.personassignment.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Transactional
    public PersonResponseDTO createPerson(PersonRequestDTO personRequestDTO) {
        Person person = personMapper.toEntity(personRequestDTO);

        Person savedPerson = personRepository.save(person);

        return personMapper.toDto(savedPerson);
    }

    public List<PersonResponseDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }
}
