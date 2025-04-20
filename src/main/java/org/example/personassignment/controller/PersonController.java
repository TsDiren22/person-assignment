package org.example.personassignment.controller;

import jakarta.validation.Valid;
import org.example.personassignment.service.PersonService;
import org.example.personassignment.dto.PersonRequestDTO;
import org.example.personassignment.dto.PersonResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAllPersons(
            @RequestParam(required = false) String sortBy) {

        if ("name".equals(sortBy)) {
            return ResponseEntity.ok(personService.getAllPersonsSortedByName());
        } else if ("id".equals(sortBy)) {
            return ResponseEntity.ok(personService.getAllPersonsSortedById());
        }
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@Valid @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO responseDTO = personService.createPerson(personRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(@PathVariable Long id, @Valid @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO responseDTO = personService.updatePerson(id, personRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/partner-and-three-children-one-underage")
    public ResponseEntity<String> getPersonsWithPartnerAndThreeChildren() {
        String base64EncodedCsv = personService.getPersonsWithPartnerAndThreeChildrenWithOneUnderageAsBase64Csv();
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(base64EncodedCsv);
    }
}
