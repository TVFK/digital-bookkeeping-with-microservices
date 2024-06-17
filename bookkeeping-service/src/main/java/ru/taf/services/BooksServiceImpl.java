package ru.taf.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taf.entities.Book;
import ru.taf.entities.Person;
import ru.taf.repositories.BooksRepository;
import ru.taf.repositories.PeopleRepository;
import ru.taf.services.exceptions.BookAlreadyTakenException;
import ru.taf.services.exceptions.BookNotFoundException;
import ru.taf.services.exceptions.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    public List<Book> findAllBooks(String filter, int pageNumber, int booksPerPage, boolean sort_by_year) {
        Pageable pageable;
        if (sort_by_year) {
            pageable = PageRequest.of(pageNumber, booksPerPage, Sort.by("creationYear"));
        } else {
            pageable = PageRequest.of(pageNumber, booksPerPage);
        }

        if (filter != null && !filter.isBlank()) {
            return booksRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%", pageable);
        } else {
            return booksRepository.findAllBooks(pageable);
        }
    }

    public Book findBook(Integer bookId) {
        return booksRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book with id: %d not found ".formatted(bookId)));
    }

    @Transactional
    public Book saveBook(Book book) {
        return booksRepository.save(book);
    }

    @Transactional
    public void updateBook(Integer id, Book updatedBooks) {
        updatedBooks.setId(id);
        booksRepository.save(updatedBooks);
    }

    @Transactional
    public void deleteBook(Integer id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setBook(Integer personId, Integer bookId) {
        Person person = peopleRepository.findById(personId).orElseThrow(() ->
                new PersonNotFoundException("Person with id: %d not found".formatted(personId)));
        Book book = booksRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book with id: %d not found ".formatted(bookId)));

        if(book.getOwner() != null){
            throw new BookAlreadyTakenException("Book with id: %d already taken".formatted(bookId));
        }
        book.setOwner(person);
        book.setTimeTakeBook(LocalDateTime.now());
        booksRepository.save(book);
    }

    @Transactional
    public void liberateBook(Integer bookId) {
        Book book = booksRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book with id: %d not found ".formatted(bookId)));
        book.setOwner(null);
        book.setTimeTakeBook(null);
        booksRepository.save(book);
    }
}
