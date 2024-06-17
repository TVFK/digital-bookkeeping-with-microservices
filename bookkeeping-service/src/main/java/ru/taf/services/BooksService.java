package ru.taf.services;

import ru.taf.entities.Book;

import java.util.List;

public interface BooksService {

    List<Book> findAllBooks(String filter, int pageNumber, int booksPerPage, boolean sortByYear);

    Book findBook(Integer bookId);

    Book saveBook(Book book);

    void updateBook(Integer bookId, Book updatedBooks);

    void deleteBook(Integer bookId);

    void setBook(Integer personId, Integer bookId);

    void liberateBook(Integer bookId);
}
