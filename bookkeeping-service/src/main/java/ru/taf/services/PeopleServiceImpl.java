package ru.taf.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taf.entities.Person;
import ru.taf.repositories.PeopleRepository;
import ru.taf.services.exception.PersonNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int personId) {
        return peopleRepository.findById(personId).orElseThrow(() ->
                new PersonNotFoundException("Person not found" + personId));
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

}
