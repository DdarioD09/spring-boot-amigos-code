package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.SortingOrder;
import com.example.spring_boot_ddazaa.exception.DuplicateResourceException;
import com.example.spring_boot_ddazaa.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPeople(SortingOrder sort) {
        return personRepository.findAll(
                Sort.by(
                        Sort.Direction.valueOf(sort.name()),
                        "id"
                )
        );
    }

    public Person getPersonById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Person with id: " + id + " does not exists"));
    }

    public void deletePersonById(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person with id: " + id + " does not exists");
        }
        personRepository.deleteById(id);
    }

    public void addPerson(NewPersonRequest personRequest) {
        if (personRequest.email() != null && !personRequest.email().isEmpty()) {
            boolean isEmailTaken = personRepository.existsByEmail(personRequest.email());
            if (isEmailTaken) {
                throw new DuplicateResourceException("Email already exists");
            }
        }
        Person person = new Person(
                personRequest.name(),
                personRequest.age(),
                personRequest.gender(),
                personRequest.email()
        );
        personRepository.save(person);
    }

    public void updatePerson(PersonUpdateRequest request, Integer id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id: " + id + " does not exists"));

        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(person.getName())) {
            person.setName(request.name());
        }
        if (request.age() != null && request.age() != 0 && !request.age().equals(person.getAge())) {
            person.setAge(request.age());
        }
        if (request.email() != null && !request.email().isEmpty() && !request.email().equals(person.getEmail())) {
            boolean isEmailTaken = personRepository.existsByEmail(request.email());
            if (isEmailTaken) {
                throw new DuplicateResourceException("Email already exists");
            }
            person.setEmail(request.email());
        }
        personRepository.save(person);
    }
}
