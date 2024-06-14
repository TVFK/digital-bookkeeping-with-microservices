package ru.taf.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taf.entities.Book;
import ru.taf.entities.Person;
import ru.taf.services.PeopleService;

@RestController
@RequestMapping("people/{personId:\\d+}")
@RequiredArgsConstructor
public class PersonRestController {

    private final PeopleService peopleService;

    @ModelAttribute("person")
    public Person getProduct(@PathVariable("personId") int personId) {
        return peopleService.findPerson(personId);
    }

    @GetMapping
    public Person findPerson(@ModelAttribute("person") Person person) {
        return person;
    }

    @PatchMapping
    public ResponseEntity<Void> updatePerson(@PathVariable("personId") int personId,
                                       @RequestBody @Valid Person person,
                                       BindingResult bindingResult)
            throws BindException {

        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);

        peopleService.updatePerson(personId, person);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("personId") int personId) {
        peopleService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
