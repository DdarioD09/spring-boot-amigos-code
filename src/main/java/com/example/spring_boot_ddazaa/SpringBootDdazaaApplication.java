package com.example.spring_boot_ddazaa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
@RequestMapping("api/v1/person")
public class SpringBootDdazaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDdazaaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ObjectMapper objectMapper) throws JsonProcessingException {
        String personString = "{\"id\":1,\"name\":\"John Doe\",\"age\":23}";
        Person person = objectMapper.readValue(personString, Person.class);
        System.out.println(person);
        System.out.println(objectMapper.writeValueAsString(person));
        return args -> {

        };
    }

    public enum Gender {MALE, FEMALE}

    public enum SortingOrder {ASC, DESC}

//    public record Person(
//            int id,
////            @JsonGetter("foo") String name, // for change the name of the response Json
//            @JsonGetter("foo") String name,
////            @JsonIgnore int age, // to ignore the field in the response Json
//            @JsonIgnore int age,
//            Gender gender) {
//    }

    public static class Person {
        private final int id;
        private final String name;
        private final int age;
        private final Gender gender;

        public Person(int id, String name, int age, Gender gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Gender getGender() {
            return gender;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person1{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", gender=" + gender +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return id == person.id && age == person.age && Objects.equals(name, person.name) && gender == person.gender;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age, gender);
        }
    }

    public record PersonUpdateRequest(
            String name,
            Integer age
    ) {
    }

    private static AtomicInteger idCounter = new AtomicInteger(0);

    public static List<Person> people = new ArrayList<>();

    static {
        people.add(new Person(idCounter.incrementAndGet(), "John", 20, Gender.MALE));
        people.add(new Person(idCounter.incrementAndGet(), "Jane", 18, Gender.FEMALE));
        people.add(new Person(idCounter.incrementAndGet(), "Bob", 30, Gender.MALE));
    }

    @GetMapping
    public List<Person> getPeople(
            HttpMethod httpMethod, // These are method arguments that can be passed to the methods that you annotated methods
            ServletRequest request,
            ServletResponse response,
            @RequestHeader("Content-Type") String contentType,
            @RequestParam(value = "sort",
                    required = false,
                    defaultValue = "ASC") SortingOrder sort,
            @RequestParam(value = "limit",  // This is for set a limit
                    required = false,
                    defaultValue = "10") Integer limit
    ) {
        if (sort == SortingOrder.ASC) {
            System.out.println(httpMethod);
            System.out.println(request.getLocalAddr());
            System.out.println(response.isCommitted());
            System.out.println(contentType);

            return people.stream()
                    .sorted(Comparator.comparing(Person::getId))
                    .collect((Collectors.toList()));
        }
        return people.stream()
                .sorted(Comparator.comparing(Person::getId).reversed())
                .collect((Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Person>> getPersonById(@PathVariable("id") Integer id) {
        Optional<Person> person = people.stream().filter(p -> p.id == id).findFirst();
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("{id}")
    public void deletePersonById(@PathVariable("id") Integer id) {
        people.removeIf(person -> person.id == id);
    }

    @PostMapping()
    public void addPerson(@RequestBody Person person) {
        people.add(new Person(
                idCounter.incrementAndGet(),
                person.name,
                person.age,
                person.gender
        ));
    }

//    @PutMapping("{id}")
//    public void updatePerson(
//            @RequestBody Person person,
//            @PathVariable("id") Integer id
//    ) {
//        Optional<Person> personToUpdate = people.stream().filter(p -> p.id == id).findFirst();
//        if (personToUpdate.isEmpty()) {
//            return;
//        }
//        Person personUpdated = new Person(
//                personToUpdate.get().id,
//                person.name,
//                person.age,
//                personToUpdate.get().gender
//        );
//        people.set(personToUpdate.get().id, personUpdated);
//    }

    @PutMapping("{id}")
    public void updatePerson(
            @RequestBody PersonUpdateRequest request,
            @PathVariable("id") Integer id
    ) {
        // find person by id
        people.stream()
                .filter(p -> p.id == id)
                .findFirst()
                .ifPresent(p -> {
                    var index = people.indexOf(p);
                    String name = p.name;
                    int age = p.age;

                    if (request.name != null && !request.name.isEmpty() && !request.name.equals(p.name)) {
                        name = request.name;
                    }
                    if (request.age != null && request.age != 0 && !request.age.equals(p.age)) {
                        age = request.age;
                    }
                    Person person = new Person(
                            p.id,
                            name,
                            age,
                            p.gender
                    );
                    people.set(index, person);
                });
    }

}


