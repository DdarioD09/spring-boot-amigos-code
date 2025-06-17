package com.example.spring_boot_ddazaa.person;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PersonRepository {

    private final AtomicInteger idCounter = new AtomicInteger(0);

    private final List<Person> people = new ArrayList<>();

    {
        people.add(new Person(idCounter.incrementAndGet(), "John", 20, Gender.MALE));
        people.add(new Person(idCounter.incrementAndGet(), "Jane", 18, Gender.FEMALE));
        people.add(new Person(idCounter.incrementAndGet(), "Bob", 30, Gender.MALE));
    }

    public AtomicInteger getIdCounter() {
        return idCounter;
    }

    public List<Person> getPeople() {
        return people;
    }
}
