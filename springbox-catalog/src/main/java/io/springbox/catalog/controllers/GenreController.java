package io.springbox.catalog.controllers;

import io.springbox.catalog.domain.Genre;
import io.springbox.catalog.repositories.GenreRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class GenreController {

    @Autowired
    GenreRepository genreRepository;

    @HystrixCommand(
		fallbackMethod = "defaultGenres",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
	)
    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public Iterable<Genre> genres() {
        return genreRepository.findAll();
    }

    @HystrixCommand(
		fallbackMethod = "defaultGenre",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
	)
    @RequestMapping(value = "/genres/{mlId}", method = RequestMethod.GET)
    public Genre genre(@PathVariable String mlId) {
        return genreRepository.findByMlId(mlId);
    }
    
    public Iterable<Genre> defaultGenres() {
    	return Collections.emptyList();
    }
    
    public Genre defaultGenre(String mlId) {
    	final Genre g = new Genre();
    	
    	g.setMlId(mlId);
    	g.setName("Unknown");
    	
    	return g;
    }

}
