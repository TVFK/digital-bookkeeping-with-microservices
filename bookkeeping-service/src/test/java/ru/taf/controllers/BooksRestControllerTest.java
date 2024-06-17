package ru.taf.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.entities.Book;
import ru.taf.services.BooksService;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksRestControllerTest {

    @Mock
    BooksService booksService;

    @InjectMocks
    BooksRestController controller;

    @Test
    void findBooks_ReturnsListOfBooks() {
        // given
        String filter = "Книжка";
        List<Book> books = List.of(new Book(1, "title", "author", 2024, null, null),
                new Book(2, "title2", "author2", 2024, null, null));

        doReturn(books)
                .when(booksService)
                .findAllBooks(filter, 0, 10, false);
        // when
        var result = controller.findBooks(filter, 0, 10, false);

        // then
        assertEquals(books, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void createProduct_RequestIsValid_ReturnsNoContent() throws BindException {
        // given
        Book book = new Book(1, "title", "author", 2024, null, null);
        var bindingResult = new MapBindingResult(Map.of(), "book");
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        doReturn(book)
                .when(this.booksService).saveBook(book);

        // when
        var result = controller.create(book, bindingResult, uriComponentsBuilder);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("http://localhost/books/1"), result.getHeaders().getLocation());
        assertEquals(book, result.getBody());

        verify(this.booksService).saveBook(book);
        verifyNoMoreInteractions(booksService);
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsBadRequest() {
        // given
        Book book = new Book(1, "   ", "author", 2024, null, null);
        var bindingResult = new MapBindingResult(Map.of(), "book");
        bindingResult.addError(new FieldError("book", "title", "error"));
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // when
        var exception = assertThrows(BindException.class,
                () -> this.controller.create(book, bindingResult, uriComponentsBuilder));

        // then
        assertEquals(List.of(new FieldError("book", "title", "error")), exception.getAllErrors());
        verifyNoInteractions(this.booksService);
    }

    @Test
    void createProduct_RequestIsInvalidAndBindResultIsBindException_ReturnsBadRequest() {
        // given
        Book book = new Book(1, "   ", "author", 2024, null, null);
        var bindingResult = new BindException(new MapBindingResult(Map.of(), "payload"));
        bindingResult.addError(new FieldError("book", "title", "error"));
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // when
        var exception = assertThrows(BindException.class,
                () -> this.controller.create(book, bindingResult, uriComponentsBuilder));

        // then
        assertEquals(List.of(new FieldError("book", "title", "error")), exception.getAllErrors());
        verifyNoInteractions(this.booksService);
    }
}