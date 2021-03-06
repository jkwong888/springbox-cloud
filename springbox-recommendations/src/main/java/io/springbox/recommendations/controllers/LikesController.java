package io.springbox.recommendations.controllers;

import io.springbox.recommendations.domain.Likes;
import io.springbox.recommendations.repositories.LikesRepository;

import java.security.Principal;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class LikesController {

    Log log = LogFactory.getLog(LikesController.class);

    @Autowired
    LikesRepository likesRepository;

    @RequestMapping(value = "/likes", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultLikes",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
    )
    public Iterable<Likes> likes(Principal principal) {
        return likesRepository.likesFor(principal.getName());
    }

    @RequestMapping(value = "/does/{userName}/like/{mlId}", method = RequestMethod.GET)
    @HystrixCommand(
		fallbackMethod = "defaultLikesFor",
		commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
		}
	)
    public ResponseEntity<Boolean> likesFor(@PathVariable("mlId") String mlId, @PathVariable("userName") String userName) {
        log.debug(String.format("/does/%s/like/%s endpoint requested!", userName, mlId));
        int likes = likesRepository.likesFor(mlId, userName).size();
        log.debug(String.format("Result of %s like %s: %s", userName, mlId, likes));
        if (likes > 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
    
    public Iterable<Likes> defaultLikes(Principal principal) {
    	return Collections.emptyList();
    }
    
    public ResponseEntity<Boolean> defaultLikesFor(String mlId, String userName) {
    	return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
