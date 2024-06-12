package ru.taf.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.entities.Book;
import ru.taf.services.BooksService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    @GetMapping
    public ResponseEntity<Page<Book>> findBooks(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "books_per_page", required = false, defaultValue = "10") int booksPerPage,
            @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year) {

        Page<Book> books = booksService.findAllBooks(filter, page, booksPerPage, sort_by_year);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Book book,
                                    BindingResult bindingResult,
                                    UriComponentsBuilder uriComponentsBuilder)
            throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Book createdBook = booksService.save(book);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/books/{bookId}")
                            .build(Map.of("bookId", createdBook.getId())))
                    .body(createdBook);
        }
    }
}
