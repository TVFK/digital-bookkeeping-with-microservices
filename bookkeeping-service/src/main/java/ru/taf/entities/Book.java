package ru.taf.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "Book")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message =  "Name mustn't be empty")
    @Size(min = 2, max = 100, message = "name must be between 2 and 50 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author name mustn't be empty")
    @Size(min = 2, max = 50, message = "Author name must be between 2 and 50 characters")
    @Column(name = "author_name")
    private String author;

    @Min(value = 0, message = "Year of creation must be greater than 0")
    @Column(name = "year_creation")
    private int creationYear;

    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "book_take_timestamp")
    public LocalDateTime timeTakeBook;
}
