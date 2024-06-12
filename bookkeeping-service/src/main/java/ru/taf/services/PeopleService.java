package ru.taf.services;

import ru.taf.entities.Person;

import java.util.List;
import java.util.Optional;

public interface PeopleService {
    List<Person> findAll();
    Person findOne(int id);
    void save(Person person);
    void update(int id, Person updatedPerson);
    void delete(int id);
}
