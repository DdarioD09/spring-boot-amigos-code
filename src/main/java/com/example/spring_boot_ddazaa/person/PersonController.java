package com.example.spring_boot_ddazaa.person;

import com.example.spring_boot_ddazaa.SortingOrder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/people")
public class PersonController {

    private final PersonService personService;
    private final Validator validator;

    public PersonController(PersonService personService, Validator validator) {
        this.personService = personService;
        this.validator = validator;
    }

    @GetMapping
    public List<Person> getPeople(
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") SortingOrder sort
    ) {
        return personService.getPeople(sort);
    }

    @GetMapping("{id}")
    public ResponseEntity<Person> getPersonById(@Valid @Positive @PathVariable("id") Integer id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @DeleteMapping("{id}")
    public void deletePersonById(@Valid @Positive @PathVariable("id") Integer id) {
        personService.deletePersonById(id);
    }

    @PostMapping()
    public void addPerson(@Valid @RequestBody NewPersonRequest person) {
//    public void addPerson(@RequestBody NewPersonRequest person) {
//        Control validation through  variable declaration
//        Set<ConstraintViolation<NewPersonRequest>> validate = this.validator.validate(person);
//        validate.forEach(error -> System.out.println(error.getMessage()));
//        if(!validate.isEmpty()) {
//            throw new ConstraintViolationException(validate);
//        }
        personService.addPerson(person);
    }

    @PutMapping("{id}")
    public void updatePerson(@RequestBody PersonUpdateRequest request, @PathVariable("id") Integer id) {
        personService.updatePerson(request, id);
    }
}
