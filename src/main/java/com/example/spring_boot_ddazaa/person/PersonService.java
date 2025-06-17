package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.SortingOrder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public Optional<Person> getPersonById(Integer id) {
        return personRepository.getPeople().stream().filter(p -> p.id() == id).findFirst();
    }

    public void deletePersonById(Integer id) {
        personRepository.getPeople().removeIf(person -> person.id() == id);
    }

    public void addPerson(Person person) {
        personRepository.getPeople().add(new Person(
                personRepository.getIdCounter().incrementAndGet(),
                person.name(),
                person.age(),
                person.gender()
        ));
    }

    public void updatePerson(PersonUpdateRequest request, Integer id) {
        // find person by id
        personRepository.getPeople().stream()
                .filter(p -> p.id() == id)
                .findFirst()
                .ifPresent(p -> {
                    var index = personRepository.getPeople().indexOf(p);
                    String name = p.name();
                    int age = p.age();

                    if (request.name() != null && !request.name().isEmpty() && !request.name().equals(p.name())) {
                        name = request.name();
                    }
                    if (request.age() != null && request.age() != 0 && !request.age().equals(p.age())) {
                        age = request.age();
                    }
                    Person person = new Person(
                            p.id(),
                            name,
                            age,
                            p.gender()
                    );
                    personRepository.getPeople().set(index, person);
                });
    }
}
