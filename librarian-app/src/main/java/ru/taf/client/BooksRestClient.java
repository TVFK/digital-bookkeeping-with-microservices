package ru.taf.client;

import ru.taf.entities.Book;

import java.util.List;

public interface BooksRestClient {
    List<Book> findAllBooks(String filter, Integer pageNumber, Integer booksPerPage, boolean sortByYear);

    Book findBook(Integer bookId);

    Book createBook(Book book);

    void updateBook(Integer bookId, Book updatedBooks);

    void deleteBook(Integer bookId);

    void setBook(Integer personId, Integer bookId);

    void liberateBook(Integer bookId);
}
