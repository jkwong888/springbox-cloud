package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Person;
import io.springbox.recommendations.repositories.PersonRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value = "/people", method = RequestMethod.GET)
    @HystrixCommand(
  		fallbackMethod = "defaultPeople",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
	)
    public Iterable<Person> people() {
        return personRepository.findAll();
    }

    @RequestMapping(value = "/people", method = RequestMethod.POST)
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }
    
    public Iterable<Person> defaultPeople() {
    	return Collections.emptyList();
    }

}
