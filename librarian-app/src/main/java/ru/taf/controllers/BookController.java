package ru.taf.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.taf.client.BooksRestClient;
import ru.taf.client.PeopleRestClient;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.entities.Book;

@Controller
@RequestMapping("/books/{bookId:\\d+}")
@AllArgsConstructor
public class BookController {

    private final BooksRestClient booksRestClient;

    private final PeopleRestClient peopleRestClient;

    @ModelAttribute("book")
    public Book book(@PathVariable("bookId") int bookId) {
        return booksRestClient.findBook(bookId);
    }

    @GetMapping
    public String getBook(Model model) {
        model.addAttribute("people", peopleRestClient.findAllPeople(null));
        return "books/book";
    }

    @GetMapping("edit")
    public String getBookEditPage() {
        return "books/edit";
    }

    @PostMapping("edit")
    public String updateBook(@PathVariable("bookId") int bookId,
                             @ModelAttribute(name = "book", binding = false) Book updatedBook,
                             Model model,
                             HttpServletResponse response) {
        try {
            booksRestClient.updateBook(bookId, updatedBook);
            return "redirect:/books/%d".formatted(bookId);
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("book", updatedBook);
            model.addAttribute("errors", exception.getErrors());
            return "books/edit";
        }
    }

    @PostMapping("/set")
    public String setBook(@PathVariable("bookId") int bookId,
                          @RequestParam("personId") int personId) {
        booksRestClient.setBook(personId, bookId);
        return "redirect:/books/%d".formatted(bookId);
    }

    @PostMapping("/liberate")
    public String liberateBook(@PathVariable("bookId") int bookId) {
        booksRestClient.liberateBook(bookId);
        return "redirect:/books/%d".formatted(bookId);
    }

    @PostMapping("delete")
    public String deleteBook(@ModelAttribute("product") Book book) {
        booksRestClient.deleteBook(book.getId());
        return "redirect:/books/list";
    }
}
