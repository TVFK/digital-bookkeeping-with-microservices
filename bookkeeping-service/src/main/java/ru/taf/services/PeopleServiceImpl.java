package ru.taf.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taf.entities.Person;
import ru.taf.repositories.PeopleRepository;
import ru.taf.services.exceptions.PersonNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    public List<Person> findAllPeople(String filter) {
        if(filter != null && !filter.isBlank()){
            return peopleRepository.findAllByNameLikeIgnoreCase(filter);
        } else {
            return peopleRepository.findAll();
        }
    }

    public Person findPerson(Integer personId) {
        return peopleRepository.findById(personId).orElseThrow(() ->
                new PersonNotFoundException("Person with id: %d not found".formatted(personId)));
    }

    @Transactional
    public Person createPerson(Person person) {
        return peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(Integer personId, Person updatedPerson) {
        updatedPerson.setId(personId);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(Integer personId) {
        peopleRepository.deleteById(personId);
    }

}
