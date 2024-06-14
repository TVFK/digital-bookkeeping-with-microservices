package ru.taf.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.taf.entities.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByTitleLikeIgnoreCase(String filter, Pageable pageable);

    @Query("SELECT b FROM Book b")
    List<Book> findAllBooks(Pageable pageable);
}
