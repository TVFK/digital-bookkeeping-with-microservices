package ru.taf.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taf.entities.Book;
import ru.taf.entities.Person;
import ru.taf.services.BooksService;
import ru.taf.services.PeopleService;

@RestController
@RequestMapping("/books/{bookId:\\d+}")
@AllArgsConstructor
public class BookController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @ModelAttribute("book")
    public Book getBook(@PathVariable("bookId") int bookId) {
        return booksService.findBook(bookId);
    }

    @GetMapping
    public Book findBook(@ModelAttribute("book") Book book) {
        return book;
    }

    @PatchMapping
    public ResponseEntity<?> update(@PathVariable("bookId") int bookId,
                                    @Valid @RequestBody Book book,
                                    BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.booksService.update(bookId, book);
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @PatchMapping("/set")
    public ResponseEntity<Void> setBook(@PathVariable("bookId") int bookId,
                                        @RequestParam("personId") int personId) {
        booksService.setBook(personId, bookId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/liberate")
    public ResponseEntity<Void> liberateBook(@PathVariable("bookId") int bookId) {
        booksService.liberateBook(bookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("bookId") int bookId) {
        booksService.delete(bookId);
        return ResponseEntity.noContent().build();
    }
}
