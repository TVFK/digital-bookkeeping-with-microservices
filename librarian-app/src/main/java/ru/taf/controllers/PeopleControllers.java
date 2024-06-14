package ru.taf.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.taf.client.PeopleRestClient;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.entities.Person;

@Controller
@RequestMapping("/people")
@AllArgsConstructor
public class PeopleControllers {

    private final PeopleRestClient peopleRestClient;

    @GetMapping("list")
    public String findAll(@RequestParam(name = "filter", required = false) String filter,
                          Model model) {
        model.addAttribute("people", peopleRestClient.findAllPeople(filter));
        model.addAttribute("filter", filter);
        return "people/list";
    }

    @GetMapping("create")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new_person";
    }

    @PostMapping("create")
    public String create(@ModelAttribute("person") Person person,
                         Model model,
                         HttpServletResponse response) {
        try {
            Person createdPerson = peopleRestClient.createPerson(person);
            return "redirect:/people/%d".formatted(createdPerson.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("person", person);
            model.addAttribute("errors", exception.getErrors());
            return "people/new_person";
        }
    }
}
