package io.springbox.catalog.controllers;

import io.springbox.catalog.domain.Genre;
import io.springbox.catalog.domain.Movie;
import io.springbox.catalog.repositories.GenreRepository;
import io.springbox.catalog.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultMovies",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    @Transactional
    public Iterable<Movie> movies() {
        return movieRepository.findAll();
    }

    @RequestMapping(value = "/movies/{mlId}", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultMovieByMlId",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    @Transactional
    public Movie movie(@PathVariable String mlId) {
        return movieRepository.findByMlId(mlId);
    }

    @RequestMapping(value = "/movies/genre/{genreMlId}", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultMoviesByGenre",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    public List<Movie> moviesByGenreMlId (@PathVariable String genreMlId) {
        Genre genre = genreRepository.findByMlId(genreMlId);
        return movieRepository.findByGenre(genre);
    }
    
    public Iterable<Movie> defaultMovies() {
    	return Collections.emptyList();
    }
     
    public List<Movie> defaultMoviesByGenre(String genreMlId) {
    	return Collections.emptyList();
    }
    
    public Movie defaultMovieByMlId(String mlId) {
    	final Movie movie = new Movie();
    	movie.setMlId(mlId);
    	movie.setTitle("Unknown");
    	movie.setGenres(new ArrayList<Genre>());
    	
    	return movie;
    }
}
