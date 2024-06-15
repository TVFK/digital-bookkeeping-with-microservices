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

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BooksRestController {

    private final BooksService booksService;

    @GetMapping
    public ResponseEntity<List<Book>> findBooks(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "booksPerPage", required = false, defaultValue = "10") int booksPerPage,
            @RequestParam(value = "sortByYear", required = false) boolean sortByYear) {

        List<Book> books = booksService.findAllBooks(filter, page, booksPerPage, sortByYear);
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
                            .path("/books/{bookId}")
                            .buildAndExpand(createdBook.getId())
                            .toUri())
                    .body(createdBook);
        }
    }
}
