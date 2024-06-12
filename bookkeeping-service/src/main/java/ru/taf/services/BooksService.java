package ru.taf.services;

import org.springframework.data.domain.Page;
import ru.taf.entities.Book;

public interface BooksService {

    Page<Book> findAllBooks(String filter, int pageNumber, int booksPerPage, boolean sortByYear);

    Book findBook(int bookId);

    Book save(Book book);

    void update(int bookId, Book updatedBooks);

    void delete(int bookId);

    void setBook(int personId, int bookId);

    void liberateBook(int bookId);
}