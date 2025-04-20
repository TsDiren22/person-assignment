package org.example.personassignment.service;

import org.example.personassignment.entity.Person;
import org.example.personassignment.repository.PersonRepository;
import org.example.personassignment.utility.CsvUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceCsvTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person parent1;
    private Person parent2;
    private Person parent3;
    private Person underageChild;
    private Person adultChild1;
    private Person adultChild2;
    private Person mismatchedParentChild;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();

        parent1 = createPerson(1L, "Parent 1", today.minusYears(30), null, null, 2L);
        parent2 = createPerson(2L, "Parent 2", today.minusYears(29), null, null, 1L);
        parent3 = createPerson(3L, "Parent 3", today.minusYears(28), null, null, null);

        underageChild = createPerson(4L, "Underage Child", today.minusYears(10), 1L, 2L, null);
        adultChild1 = createPerson(5L, "Adult Child 1", today.minusYears(20), 1L, 2L, null);
        adultChild2 = createPerson(6L, "Adult Child 2", today.minusYears(19), 1L, 2L, null);

        mismatchedParentChild = createPerson(8L, "Mismatched Parent Child", today.minusYears(15), 1L, 3L, null);
    }

    private Person createPerson(Long id, String name, LocalDate birthDate,
                                Long parent1Id, Long parent2Id, Long partnerId) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setBirthDate(birthDate);

        if (parent1Id != null) {
            Person parent1 = new Person();
            parent1.setId(parent1Id);
            person.setParent1(parent1);
        }

        if (parent2Id != null) {
            Person parent2 = new Person();
            parent2.setId(parent2Id);
            person.setParent2(parent2);
        }

        if (partnerId != null) {
            Person partner = new Person();
            partner.setId(partnerId);
            person.setPartner(partner);
        }

        return person;
    }

    @Test
    void shouldIncludeParents_WhenOneUnderageAndTwoAdultChildrenWithSameParents() {
        // Arrange
        List<Person> allPersons = Arrays.asList(
                parent1, parent2,
                underageChild, adultChild1, adultChild2
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        // Act
        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(parent1));
        assertTrue(result.contains(parent2));
    }

    @Test
    void shouldExclude_WhenOnlyOneParent() {
        List<Person> allPersons = Arrays.asList(
                parent3,
                underageChild, adultChild1, adultChild2
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExclude_WhenNoUnderageChildren() {
        List<Person> allPersons = Arrays.asList(
                parent1, parent2,
                adultChild1, adultChild2
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExclude_WhenTooManyUnderageChildren() {
        Person anotherUnderageChild = createPerson(9L, "Another Underage",
                LocalDate.now().minusYears(8), 1L, 2L, null);

        List<Person> allPersons = Arrays.asList(
                parent1, parent2,
                underageChild, anotherUnderageChild, adultChild1
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExclude_WhenChildrenHaveDifferentParents() {
        List<Person> allPersons = Arrays.asList(
                parent1, parent2, parent3,
                underageChild, adultChild1, mismatchedParentChild
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExclude_WhenNotEnoughAdultChildren() {
        List<Person> allPersons = Arrays.asList(
                parent1, parent2,
                underageChild, adultChild1
        );
        when(personRepository.findAll()).thenReturn(allPersons);

        List<Person> result = personService.getPersonsWithPartnerAndThreeChildren();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGenerateCsv_ForValidParentChildCombination() {
        // Arrange
        List<Person> allPersons = Arrays.asList(
                parent1, parent2,
                underageChild, adultChild1, adultChild2
        );

        String expectedCsv = "1,Parent 1," + parent1.getBirthDate() + "\n" +
                "2,Parent 2," + parent2.getBirthDate() + "\n";
        String expectedBase64 = Base64.getEncoder().encodeToString(expectedCsv.getBytes());

        when(personRepository.findAll()).thenReturn(allPersons);

        try (MockedStatic<CsvUtils> csvUtilsMock = mockStatic(CsvUtils.class)) {
            csvUtilsMock.when(() -> CsvUtils.personsToCsv(Arrays.asList(parent1, parent2)))
                    .thenReturn(expectedCsv);
            csvUtilsMock.when(() -> CsvUtils.encodeToBase64(expectedCsv))
                    .thenReturn(expectedBase64);

            // Act
            String result = personService.getPersonsWithPartnerAndThreeChildrenWithOneUnderageAsBase64Csv();

            // Assert
            assertEquals(expectedBase64, result);
        }
    }

    @Test
    void shouldReturnEmptyCsv_ForInvalidCombinations() {
        testInvalidCombination(Arrays.asList(parent1, parent2, underageChild));
        testInvalidCombination(Arrays.asList(parent1, parent2, adultChild1, adultChild2));
        testInvalidCombination(Arrays.asList(parent1, parent2, parent3, underageChild, adultChild1, adultChild2)); // Extra parent
    }

    private void testInvalidCombination(List<Person> testData) {
        when(personRepository.findAll()).thenReturn(testData);

        try (MockedStatic<CsvUtils> csvUtilsMock = mockStatic(CsvUtils.class)) {
            csvUtilsMock.when(() -> CsvUtils.personsToCsv(anyList()))
                    .thenReturn("");
            csvUtilsMock.when(() -> CsvUtils.encodeToBase64(""))
                    .thenReturn("");

            // Act
            String result = personService.getPersonsWithPartnerAndThreeChildrenWithOneUnderageAsBase64Csv();

            // Assert
            assertEquals("", result);
        }
    }

}