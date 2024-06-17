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
import ru.taf.entities.Book;
import ru.taf.services.BooksService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    @Mock
    BooksService booksService;

    @InjectMocks
    BookRestController controller;

    @Test
    void getProduct_BookExist_ReturnsBook() {
        // given
        var book = new Book(1, "title", "author", 2024, null, null);
        doReturn(book).when(booksService)
                .findBook(1);
        // when
        var result = controller.getBook(1);

        // then
        assertEquals(result, book);

        verify(booksService).findBook(1);
        verifyNoMoreInteractions(booksService);
    }

    @Test
    void findBook_ReturnsProduct() {
        // given
        var book = new Book(1, "title", "author", 2024, null, null);
        // when
        var result = controller.findBook(book);
        // then
        assertEquals(result, book);
    }

    @Test
    void updateBook_RequestIsValid_ReturnsNoContent() throws BindException {
        // given
        var book = new Book(1, "title", "author", 2024, null, null);
        var bindingResult = new MapBindingResult(Map.of(), "book");

        // when
        var result = controller.updateBook(1, book, bindingResult);

        // then
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);

        verify(booksService).updateBook(1, book);
        verifyNoMoreInteractions(booksService);
    }

    @Test
    void updateProduct_RequestIsInvalid_ReturnsBadRequest() {
        // given
        var book = new Book(1, "title", "author", 2024, null, null);
        var bindingResult = new MapBindingResult(Map.of(), "book");
        bindingResult.addError(new FieldError("book", "title", "error"));

        // when
        var exception = assertThrows(BindException.class,
                () -> this.controller.updateBook(1, book, bindingResult));

        // then
        assertEquals(List.of(new FieldError("book", "title", "error")), exception.getAllErrors());
        verifyNoInteractions(this.booksService);
    }

    @Test
    void updateProduct_RequestIsInvalidAndBindResultIsBindException_ReturnsBadRequest() {
        // given
        var book = new Book(1, "title", "author", 2024, null, null);
        var bindingResult = new BindException(new MapBindingResult(Map.of(), "book"));
        bindingResult.addError(new FieldError("book", "title", "error"));

        // when
        var exception = assertThrows(BindException.class,
                () -> this.controller.updateBook(1, book, bindingResult));

        // then
        assertEquals(List.of(new FieldError("book", "title", "error")), exception.getAllErrors());
        verifyNoInteractions(this.booksService);
    }

    @Test
    void deleteProduct_ReturnsNoContent() {
        // given

        // when
        var result = this.controller.deleteBook(1);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(this.booksService).deleteBook(1);
    }
}