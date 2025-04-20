package org.example.personassignment.service;

import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.example.personassignment.entity.Person;
import org.example.personassignment.mapper.PersonMapper;
import org.example.personassignment.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    private Person person;
    private PersonRequestDTO requestDTO;
    private PersonResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        requestDTO = new PersonRequestDTO(
                "John Doe",
                LocalDate.of(1990, 1, 1),
                101L,
                102L,
                null
        );

        responseDTO = new PersonResponseDTO(
                1L,
                "John Doe",
                LocalDate.of(1990, 1, 1),
                101L,
                102L,
                null
        );
    }

    @Test
    void createPerson_ShouldReturnSavedPerson() {
        // Arrange
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(responseDTO);

        // Act
        PersonResponseDTO result = personService.createPerson(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(personMapper).toEntity(requestDTO);
        verify(personRepository).save(person);
        verify(personMapper).toDto(person);
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        // Arrange
        Person person2 = new Person();
        person2.setId(2L);
        person2.setName("Jane Doe");

        PersonResponseDTO responseDTO2 = new PersonResponseDTO(
                2L, "Jane Doe", LocalDate.now(), null, null, null
        );

        when(personRepository.findAll()).thenReturn(Arrays.asList(person, person2));
        when(personMapper.toDto(person)).thenReturn(responseDTO);
        when(personMapper.toDto(person2)).thenReturn(responseDTO2);

        // Act
        List<PersonResponseDTO> result = personService.getAllPersons();

        // Assert
        assertEquals(2, result.size());
        assertEquals(responseDTO, result.get(0));
        assertEquals(responseDTO2, result.get(1));
        verify(personRepository).findAll();
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldReturnUpdatedPerson() {
        // Arrange
        PersonRequestDTO updateDTO = new PersonRequestDTO(
                "Updated Name",
                LocalDate.of(1995, 1, 1),
                201L,
                202L,
                300L
        );

        PersonResponseDTO updatedResponseDTO = new PersonResponseDTO(
                1L,
                "Updated Name",
                LocalDate.of(1995, 1, 1),
                201L,
                202L,
                300L
        );

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(updatedResponseDTO);

        // Act
        PersonResponseDTO result = personService.updatePerson(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedResponseDTO, result);
        verify(personRepository).findById(1L);
        verify(personMapper).updateEntity(updateDTO, person);
        verify(personRepository).save(person);
    }

    @Test
    void updatePerson_WhenPersonNotFound_ShouldThrowException() {
        // Arrange
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                personService.updatePerson(999L, requestDTO));
        verify(personRepository).findById(999L);
        verify(personRepository, never()).save(any());
    }

    @Test
    void deletePerson_WhenPersonExists_ShouldDeletePerson() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // Act
        personService.deletePerson(1L);

        // Assert
        verify(personRepository).findById(1L);
        verify(personRepository).delete(person);
    }

    @Test
    void deletePerson_WhenPersonNotFound_ShouldThrowException() {
        // Arrange
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                personService.deletePerson(999L));
        verify(personRepository).findById(999L);
        verify(personRepository, never()).delete(any());
    }

    @Test
    void getAllPersonsSortedByName_ShouldReturnSortedList() {
        // Arrange
        Person personA = new Person();
        personA.setId(1L);
        personA.setName("Alice");

        Person personB = new Person();
        personB.setId(2L);
        personB.setName("Bob");

        PersonResponseDTO responseA = new PersonResponseDTO(1L, "Alice", null, null, null, null);
        PersonResponseDTO responseB = new PersonResponseDTO(2L, "Bob", null, null, null, null);

        when(personRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(personA, personB));
        when(personMapper.toDto(personA)).thenReturn(responseA);
        when(personMapper.toDto(personB)).thenReturn(responseB);

        // Act
        List<PersonResponseDTO> result = personService.getAllPersonsSortedByName();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).name());
        assertEquals("Bob", result.get(1).name());
        verify(personRepository).findAllByOrderByNameAsc();
    }

    @Test
    void getAllPersonsSortedById_ShouldReturnSortedList() {
        // Arrange
        Person person1 = new Person();
        person1.setId(1L);
        person1.setName("First");

        Person person2 = new Person();
        person2.setId(2L);
        person2.setName("Second");

        PersonResponseDTO response1 = new PersonResponseDTO(1L, "First", null, null, null, null);
        PersonResponseDTO response2 = new PersonResponseDTO(2L, "Second", null, null, null, null);

        when(personRepository.findAllByOrderByIdAsc()).thenReturn(Arrays.asList(person1, person2));
        when(personMapper.toDto(person1)).thenReturn(response1);
        when(personMapper.toDto(person2)).thenReturn(response2);

        // Act
        List<PersonResponseDTO> result = personService.getAllPersonsSortedById();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
        verify(personRepository).findAllByOrderByIdAsc();
    }
}