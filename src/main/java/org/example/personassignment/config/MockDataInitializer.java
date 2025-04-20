package org.example.personassignment.config;

import org.example.personassignment.entity.Person;
import org.example.personassignment.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class MockDataInitializer {

    private static final Logger LOGGER = Logger.getLogger(MockDataInitializer.class.getName());

    @Bean
    @Order(2)
    public CommandLineRunner insertMockData(PersonRepository personRepository) {
        return args -> {
            try {
                if (personRepository.count() > 2) {
                    LOGGER.info("Mock data already exists, skipping initialization");
                    return;
                }

                Map<String, Person> people = new HashMap<>();

                Person john = createPerson("John Smith", LocalDate.of(1980, 5, 15), personRepository);
                Person mary = createPerson("Mary Smith", LocalDate.of(1982, 8, 22), personRepository);

                setPartners(john, mary, personRepository);

                Person emma = createPerson("Emma Smith", LocalDate.of(2005, 3, 10), personRepository);
                Person lucas = createPerson("Lucas Smith", LocalDate.of(2001, 7, 5), personRepository);
                Person lily = createPerson("Lily Smith", LocalDate.of(2018, 11, 28), personRepository);

                setParents(emma, john, mary, personRepository);
                setParents(lucas, john, mary, personRepository);
                setParents(lily, john, mary, personRepository);

                people.put("john", john);
                people.put("mary", mary);
                people.put("emma", emma);
                people.put("lucas", lucas);
                people.put("lily", lily);

                Person robert = createPerson("Robert Johnson", LocalDate.of(1975, 2, 8), personRepository);
                Person susan = createPerson("Susan Johnson", LocalDate.of(1977, 9, 14), personRepository);

                setPartners(robert, susan, personRepository);

                Person michael = createPerson("Michael Johnson", LocalDate.of(2000, 4, 12), personRepository);
                Person jennifer = createPerson("Jennifer Johnson", LocalDate.of(2002, 6, 23), personRepository);
                Person david = createPerson("David Johnson", LocalDate.of(2004, 10, 5), personRepository);

                setParents(michael, robert, susan, personRepository);
                setParents(jennifer, robert, susan, personRepository);
                setParents(david, robert, susan, personRepository);

                people.put("robert", robert);
                people.put("susan", susan);
                people.put("michael", michael);
                people.put("jennifer", jennifer);
                people.put("david", david);

                Person william = createPerson("William Brown", LocalDate.of(1983, 7, 30), personRepository);
                Person olivia = createPerson("Olivia Brown", LocalDate.of(1985, 12, 3), personRepository);

                setPartners(william, olivia, personRepository);

                Person sophia = createPerson("Sophia Brown", LocalDate.of(2010, 8, 17), personRepository);
                Person james = createPerson("James Brown", LocalDate.of(2013, 5, 9), personRepository);

                setParents(sophia, william, olivia, personRepository);
                setParents(james, william, olivia, personRepository);

                people.put("william", william);
                people.put("olivia", olivia);
                people.put("sophia", sophia);
                people.put("james", james);

                Person daniel = createPerson("Daniel Davis", LocalDate.of(1988, 11, 25), personRepository);
                Person emily = createPerson("Emily Davis", LocalDate.of(1990, 4, 18), personRepository);

                setPartners(daniel, emily, personRepository);

                Person matthew = createPerson("Matthew Davis", LocalDate.of(2015, 9, 2), personRepository);

                setParents(matthew, daniel, emily, personRepository);

                people.put("daniel", daniel);
                people.put("emily", emily);
                people.put("matthew", matthew);

                LOGGER.info("Mock data inserted successfully");
            } catch (Exception e) {
                LOGGER.severe("Error inserting mock data: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private Person createPerson(String name, LocalDate birthDate, PersonRepository personRepository) {
        Person person = new Person();
        person.setName(name);
        person.setBirthDate(birthDate);

        Person emptyParent1 = personRepository.findById(Person.EMPTY_PARENT_1_ID).orElse(null);
        Person emptyParent2 = personRepository.findById(Person.EMPTY_PARENT_2_ID).orElse(null);
        person.setParent1(emptyParent1);
        person.setParent2(emptyParent2);

        return personRepository.save(person);
    }

    private void setPartners(Person person1, Person person2, PersonRepository personRepository) {
        person1.setPartner(person2);
        person2.setPartner(person1);
        personRepository.save(person1);
        personRepository.save(person2);
    }

    private void setParents(Person child, Person parent1, Person parent2, PersonRepository personRepository) {
        child.setParent1(parent1);
        child.setParent2(parent2);
        personRepository.save(child);
    }
}