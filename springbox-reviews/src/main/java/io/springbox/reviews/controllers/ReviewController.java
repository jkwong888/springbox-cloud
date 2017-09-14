package io.springbox.reviews.controllers;

import io.springbox.reviews.domain.Review;
import io.springbox.reviews.repositories.ReviewRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.*;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ReviewController extends ResourceServerConfigurerAdapter {

    @Autowired
    ReviewRepository reviewRepository;

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "defaultReviews",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}

    )
    public Iterable<Review> reviews() {
        return reviewRepository.findAll();
    }

    @RequestMapping(value = "/reviews/{mlId}", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "defaultReviewsByMlId",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    public Iterable<Review> reviewsByMlid(@PathVariable String mlId) {
        return reviewRepository.findByMlId(mlId);
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.POST)
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        reviewRepository.save(review);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
    
    public Iterable<Review> defaultReviews() {
    	return Collections.emptyList();
    }
    
	public Iterable<Review> defaultReviewsByMlId(String mlId) {
    	return Collections.emptyList();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/reviews/**").authenticated()
                .and()
            .authorizeRequests()
                .anyRequest().permitAll();
    }
}
