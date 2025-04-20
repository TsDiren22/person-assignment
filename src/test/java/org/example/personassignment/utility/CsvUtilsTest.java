package org.example.personassignment.utility;

import org.example.personassignment.entity.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @Test
    void personsToCsv_ShouldGenerateCorrectHeader() {
        String result = CsvUtils.personsToCsv(Collections.emptyList());
        assertEquals("ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n", result);
    }

    @Test
    void personsToCsv_ShouldHandleSinglePerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        String result = CsvUtils.personsToCsv(Collections.singletonList(person));
        assertEquals(
                "ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n" +
                        "1,John Doe,1990-01-01,,,\n",
                result
        );
    }

    @Test
    void personsToCsv_ShouldHandleMultiplePersons() {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setName("John Doe");
        person1.setBirthDate(LocalDate.of(1990, 1, 1));

        Person person2 = new Person();
        person2.setId(2L);
        person2.setName("Jane Smith");
        person2.setBirthDate(LocalDate.of(1992, 5, 15));

        String result = CsvUtils.personsToCsv(Arrays.asList(person1, person2));
        assertEquals(
                "ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n" +
                        "1,John Doe,1990-01-01,,,\n" +
                        "2,Jane Smith,1992-05-15,,,\n",
                result
        );
    }

    @Test
    void personsToCsv_ShouldHandleRelationships() {
        Person parent1 = new Person();
        parent1.setId(101L);

        Person parent2 = new Person();
        parent2.setId(102L);

        Person partner = new Person();
        partner.setId(201L);

        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        person.setParent1(parent1);
        person.setParent2(parent2);
        person.setPartner(partner);

        String result = CsvUtils.personsToCsv(Collections.singletonList(person));
        assertEquals(
                "ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n" +
                        "1,John Doe,1990-01-01,101,102,201\n",
                result
        );
    }

    @Test
    void personsToCsv_ShouldEscapeSpecialCharacters() {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setName("Doe, John");
        person1.setBirthDate(LocalDate.of(1990, 1, 1));

        Person person2 = new Person();
        person2.setId(2L);
        person2.setName("Smith \"Jane\"");
        person2.setBirthDate(LocalDate.of(1992, 5, 15));

        Person person3 = new Person();
        person3.setId(3L);
        person3.setName("Line\nBreak");
        person3.setBirthDate(LocalDate.of(1995, 8, 20));

        String result = CsvUtils.personsToCsv(Arrays.asList(person1, person2, person3));
        assertEquals(
                "ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n" +
                        "1,\"Doe, John\",1990-01-01,,,\n" +
                        "2,\"Smith \"\"Jane\"\"\",1992-05-15,,,\n" +
                        "3,\"Line\nBreak\",1995-08-20,,,\n",
                result
        );
    }

    @Test
    void encodeToBase64_ShouldEncodeStringCorrectly() {
        String input = "test string";
        String result = CsvUtils.encodeToBase64(input);
        assertEquals(Base64.getEncoder().encodeToString(input.getBytes()), result);
    }

    @Test
    void encodeToBase64_ShouldHandleEmptyString() {
        assertEquals("", CsvUtils.encodeToBase64(""));
    }
}