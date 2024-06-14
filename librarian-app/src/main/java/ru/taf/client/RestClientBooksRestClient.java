package ru.taf.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.client.exceptions.BookAlreadyTakenException;
import ru.taf.client.exceptions.BookNotFoundException;
import ru.taf.entities.Book;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
public class RestClientBooksRestClient implements BooksRestClient {

    private final RestClient restClient;

    @Override
    public List<Book> findAllBooks(String filter, Integer pageNumber, Integer booksPerPage, boolean sortByYear) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("/books")
                .queryParam("filter", filter)
                .queryParam("page", pageNumber)
                .queryParam("booksPerPage", booksPerPage)
                .queryParam("sortByYear", sortByYear);

        return restClient
                .get()
                .uri(uri.toUriString())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Book>>() {});
    }

    @Override
    public Book findBook(Integer bookId) {
        try {
            return restClient.get()
                    .uri("/books/{bookId}", bookId)
                    .retrieve()
                    .body(Book.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new BookNotFoundException("Person with id: %d not found".formatted(bookId));
        }
    }

    @Override
    public Book createBook(Book book) {
        try {
            return restClient
                    .post()
                    .uri("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(book)
                    .retrieve()
                    .body(Book.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            List<String> errors = (List<String>) (problemDetail != null ? problemDetail.getProperties().get("errors") : List.of("Unknown error"));
            throw new BadRequestException(errors);
        }
    }

    @Override
    public void updateBook(Integer bookId, Book updatedBook) {
        try {
            restClient
                    .patch()
                    .uri("/books/{bookId}", bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedBook)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            List<String> errors = (List<String>) (problemDetail != null ? problemDetail.getProperties().get("errors") : List.of("Unknown error"));
            throw new BadRequestException(errors);
        }
    }

    @Override
    public void deleteBook(Integer bookId) {
        try {
            restClient
                    .delete()
                    .uri("/books/{bookId}", bookId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new BookNotFoundException("Book with id: %d not found".formatted(bookId));
        }
    }

    @Override
    public void setBook(Integer personId, Integer bookId) {
        URI uri = UriComponentsBuilder.fromUriString("/books/{bookId}/set")
                .queryParam("personId", personId)
                .build(bookId);

        try {
            restClient
                    .patch()
                    .uri(uri)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new BookNotFoundException("Book with id: %d not found".formatted(bookId));
        } catch (HttpClientErrorException.Conflict exception){
            throw new BookAlreadyTakenException("Book with id: %d already taken".formatted(bookId));
        }
    }

    @Override
    public void liberateBook(Integer bookId) {
        try {
            restClient
                    .patch()
                    .uri("/books/{bookId}/liberate", bookId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new BookNotFoundException("Book with id: %d not found".formatted(bookId));
        }
    }
}
