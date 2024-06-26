package ru.taf.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taf.entities.Book;
import ru.taf.services.BooksService;

@RestController
@RequestMapping("/books/{bookId:\\d+}")
@AllArgsConstructor
public class BookRestController {

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
    public ResponseEntity<?> updateBook(@PathVariable("bookId") int bookId,
                                        @Valid @RequestBody Book book,
                                        BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.booksService.updateBook(bookId, book);
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
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") int bookId) {
        booksService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
