package org.example.personassignment.service;

import jakarta.transaction.Transactional;
import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.example.personassignment.mapper.PersonMapper;
import org.example.personassignment.repository.PersonRepository;
import org.example.personassignment.utility.CsvUtils;
import org.example.personassignment.utility.PersonFilterUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .map(personMapper::toDto).toList();
    }

    @Transactional
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO personRequestDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        personMapper.updateEntity(personRequestDTO, person);

        Person updatedPerson = personRepository.save(person);

        return personMapper.toDto(updatedPerson);
    }

    @Transactional
    public void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        personRepository.delete(person);
    }

    public String getPersonsWithPartnerAndThreeChildrenWithOneUnderageAsBase64Csv() {
        List<Person> filteredPersons = getPersonsWithPartnerAndThreeChildren();
        String csvContent = CsvUtils.personsToCsv(filteredPersons);
        return CsvUtils.encodeToBase64(csvContent);
    }

    public List<Person> getPersonsWithPartnerAndThreeChildren() {
        List<Person> allPersons = personRepository.findAll();

        return allPersons.stream()
                .filter(PersonFilterUtils::hasPartner)
                .filter(person -> PersonFilterUtils.hasThreeChildrenWithPartner(person, allPersons))
                .filter(person -> PersonFilterUtils.hasOneChildUnder18(person, allPersons))
                .toList();
    }

    public List<PersonResponseDTO> getAllPersonsSortedByName() {
        return personRepository.findAllByOrderByNameAsc().stream()
                .map(personMapper::toDto)
                .toList();
    }

    public List<PersonResponseDTO> getAllPersonsSortedById() {
        return personRepository.findAllByOrderByIdAsc().stream()
                .map(personMapper::toDto)
                .toList();
    }


}
