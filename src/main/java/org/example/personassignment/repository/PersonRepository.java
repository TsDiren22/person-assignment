package org.example.personassignment.repository;

import org.example.personassignment.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByOrderByNameAsc();
    List<Person> findAllByOrderByIdAsc();
}
