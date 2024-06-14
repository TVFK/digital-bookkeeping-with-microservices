package ru.taf.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.client.BooksRestClient;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.entities.Book;
import ru.taf.entities.Person;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksRestClient booksRestClient;

    @GetMapping("list")
    public String index(Model model,
                        @RequestParam(name = "filter", required = false) String filter,
                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "10") int booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year){
        List<Book> books = booksRestClient.findAllBooks(filter, page, booksPerPage, sort_by_year);
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("create")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new_book";
    }
    @PostMapping("create")
    public String create(@ModelAttribute("book") Book book,
                         Model model,
                         HttpServletResponse response) {
        try {
            Book createdBook = booksRestClient.createBook(book);
            return "redirect:/books/%d".formatted(createdBook.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("book", book);
            model.addAttribute("errors", exception.getErrors());
            return "books/new_book";
        }
    }
}
