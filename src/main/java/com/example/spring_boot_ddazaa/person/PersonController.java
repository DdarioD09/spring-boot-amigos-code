package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.SortingOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getPeople(
            @RequestParam(
                    value = "sort",
                    required = false,
                    defaultValue = "ASC"
            ) SortingOrder sort
    ) {
        return personService.getPeople(sort);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Person>> getPersonById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @DeleteMapping("{id}")
    public void deletePersonById(@PathVariable("id") Integer id) {
        personService.deletePersonById(id);
    }

    @PostMapping()
    public void addPerson(@RequestBody Person person) {
        personService.addPerson(person);
    }

    @PutMapping("{id}")
    public void updatePerson(
            @RequestBody PersonUpdateRequest request,
            @PathVariable("id") Integer id
    ) {
        personService.updatePerson(request, id);
    }
}
