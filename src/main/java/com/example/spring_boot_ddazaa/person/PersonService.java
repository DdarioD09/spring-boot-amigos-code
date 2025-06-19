package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.SortingOrder;
import com.example.spring_boot_ddazaa.exception.DuplicateResourceException;
import com.example.spring_boot_ddazaa.exception.ResourceNotFoundException;
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
        if (sort == SortingOrder.ASC) {
            return personRepository.getPeople().stream()
                    .sorted(Comparator.comparing(Person::id))
                    .collect((Collectors.toList()));
        }
        return personRepository.getPeople().stream()
                .sorted(Comparator.comparing(Person::id).reversed())
                .collect((Collectors.toList()));
    }

    public Person getPersonById(Integer id) {
        return personRepository.getPeople().stream().filter(p -> p.id() == id).findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Person with id: " + id + " does not exists"));
    }

    public void deletePersonById(Integer id) {
        boolean removed = personRepository.getPeople().removeIf(person -> person.id() == id);
        if (!removed) {
            throw new ResourceNotFoundException("Person with id: " + id + " does not exists");
        }
    }

    public void addPerson(NewPersonRequest person) {
        if (person.email() != null && !person.email().isEmpty()) {
            boolean exists = personRepository.getPeople().stream()
                    .anyMatch(p -> p.email().equals(person.email()));
            if (exists) {
                throw new DuplicateResourceException("Email already exists");
            }
        }

        System.out.println("Hello from here should add the person");
        personRepository.getPeople().add(new Person(
                personRepository.getIdCounter().incrementAndGet(),
                person.name(),
                person.age(),
                person.gender(),
                person.email()
        ));
    }

    public void updatePerson(PersonUpdateRequest request, Integer id) {
        Person p = personRepository.getPeople().stream()
                .filter(person -> person.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id: " + id + " does not exists"));

        var index = personRepository.getPeople().indexOf(p);
        Person person = personToUpdate(request, p);
        personRepository.getPeople().set(index, person);
    }

    private Person personToUpdate(PersonUpdateRequest request, Person p) {
        String name = p.name();
        int age = p.age();

        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(p.name())) {
            name = request.name();
        }
        if (request.age() != null && request.age() != 0 && !request.age().equals(p.age())) {
            age = request.age();
        }
        return new Person(
                p.id(),
                name,
                age,
                p.gender(),
                p.email()
        );
    }
}
