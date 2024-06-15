package ru.taf.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.entities.Person;
import ru.taf.services.PeopleService;

import java.util.List;

@RestController
@RequestMapping("/people")
@AllArgsConstructor
public class PeopleRestControllers {

    private final PeopleService peopleService;

    @GetMapping
    public ResponseEntity<List<Person>> findAll(String filter) {
        List<Person> people = peopleService.findAllPeople(filter);
        return ResponseEntity.ok().body(people);
    }

    @PostMapping
    public ResponseEntity<Person> create(@ModelAttribute("person") @Valid Person person,
                                         BindingResult bindingResult,
                                         UriComponentsBuilder uriComponentsBuilder)
            throws BindException {

        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);

        Person createdPerson = peopleService.createPerson(person);
        return ResponseEntity
                .created(uriComponentsBuilder
                        .path("/books/{bookId}")
                        .buildAndExpand(createdPerson.getId())
                        .toUri())
                .body(createdPerson);
    }
}
