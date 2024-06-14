package ru.taf.client;

import ru.taf.entities.Person;

import java.util.List;

public interface PeopleRestClient {
    List<Person> findAllPeople(String filter);

    Person findPerson(Integer personId);

    Person createPerson(Person person);

    void updatePerson(Integer personId, Person updatedPerson);

    void deletePerson(Integer personId);
}
