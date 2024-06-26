package ru.taf.services;

import ru.taf.entities.Person;

import java.util.List;

public interface PeopleService {
    List<Person> findAllPeople(String filter);

    Person findPerson(Integer id);

    Person createPerson(Person person);

    void updatePerson(Integer personId, Person updatedPerson);

    void deletePerson(Integer personId);
}
