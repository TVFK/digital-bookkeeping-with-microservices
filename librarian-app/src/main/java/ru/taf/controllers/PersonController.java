package ru.taf.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.taf.client.BooksRestClient;
import ru.taf.client.PeopleRestClient;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.entities.Person;

@Controller
@RequestMapping("people/{personId:\\d+}")
@RequiredArgsConstructor
public class PersonController {

    private final PeopleRestClient peopleRestClient;
    private final BooksRestClient booksRestClient;

    @ModelAttribute("person")
    public Person person(@PathVariable("personId") int personId) {
        return peopleRestClient.findPerson(personId);
    }

    @GetMapping
    public String getPerson(Model model) {
        model.addAttribute("books", booksRestClient.findAllBooks(null, null, null, false));
        return "people/person";
    }

    @GetMapping("edit")
    public String getPersonEditPage() {
        return "people/edit";
    }

    @PostMapping("edit")
    public String updatePerson(@PathVariable("personId") int personId,
                                @ModelAttribute(name = "person", binding = false) Person updatedPerson,
                                Model model,
                                HttpServletResponse response) {
        try {
            this.peopleRestClient.updatePerson(personId, updatedPerson);
            return "redirect:/people/%d".formatted(personId);
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("person", updatedPerson);
            model.addAttribute("errors", exception.getErrors());
            return "people/edit";
        }
    }

    @PostMapping("delete")
    public String deletePerson(@ModelAttribute("product") Person person) {
        this.peopleRestClient.deletePerson(person.getId());
        return "redirect:/people/list";
    }
}
