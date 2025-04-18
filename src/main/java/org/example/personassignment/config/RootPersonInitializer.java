package org.example.personassignment.config;

import org.example.personassignment.entity.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

@Configuration
public class RootPersonInitializer {

    @Bean
    public CommandLineRunner insertRootPersons(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                Integer count1 = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM person WHERE id = ?", Integer.class, Person.EMPTY_PARENT_1_ID);

                if (count1 == null || count1 == 0) {
                    jdbcTemplate.update(
                            "INSERT INTO person (id, name, birth_date) VALUES (?, ?, ?)",
                            Person.EMPTY_PARENT_1_ID, "Empty Parent 1", LocalDate.of(1900, 1, 1)
                    );
                }

                Integer count2 = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM person WHERE id = ?", Integer.class, Person.EMPTY_PARENT_2_ID);

                if (count2 == null || count2 == 0) {
                    jdbcTemplate.update(
                            "INSERT INTO person (id, name, birth_date) VALUES (?, ?, ?)",
                            Person.EMPTY_PARENT_2_ID, "Empty Parent 2", LocalDate.of(1900, 1, 1)
                    );
                }
            } catch (Exception e) {
                System.err.println("Error inserting root persons: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}