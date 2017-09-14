package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Movie;
import io.springbox.recommendations.repositories.MovieRepository;

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
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultMovies",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    public Iterable<Movie> movies() {
        return movieRepository.findAll();
    }

    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }
    
    public Iterable<Movie> defaultMovies() {
    	return Collections.emptyList();
    }
    
}
