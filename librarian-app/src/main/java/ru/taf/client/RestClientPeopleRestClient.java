package ru.taf.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.taf.client.exceptions.BadRequestException;
import ru.taf.client.exceptions.PersonNotFoundException;
import ru.taf.entities.Person;

import java.net.URI;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class RestClientPeopleRestClient implements PeopleRestClient {

    private final RestClient restClient;

    @Override
    public List<Person> findAllPeople(String filter) {
        return restClient
                .get()
                .uri("/people?filter={filter}", filter)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Person findPerson(Integer personId) {
        try {
            return restClient.get()
                    .uri("/people/{personId}", personId)
                    .retrieve()
                    .body(Person.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new PersonNotFoundException("Person with id: %d not found".formatted(personId));
        }
    }

    @Override
    public Person createPerson(Person person) {
        try {
            return restClient
                    .post()
                    .uri("/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(person)
                    .retrieve()
                    .body(Person.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            List<String> errors = (List<String>) (problemDetail != null ? problemDetail.getProperties().get("errors") : List.of("Unknown error"));
            throw new BadRequestException(errors);
        }
    }

    @Override
    public void updatePerson(Integer personId, Person updatedPerson) {
        try {
            restClient
                    .patch()
                    .uri("/people/{personId}", personId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedPerson)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            List<String> errors = (List<String>) (problemDetail != null ? problemDetail.getProperties().get("errors") : List.of("Unknown error"));
            throw new BadRequestException(errors);
        }
    }

    @Override
    public void deletePerson(Integer personId) {
        try {
            restClient
                    .delete()
                    .uri("/people/{personId}", personId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new PersonNotFoundException("Person with id: %d not found".formatted(personId));
        }
    }
}
