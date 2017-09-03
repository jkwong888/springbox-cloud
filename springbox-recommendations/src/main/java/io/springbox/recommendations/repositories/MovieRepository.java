package io.springbox.recommendations.repositories;

import io.springbox.recommendations.domain.Movie;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface MovieRepository extends GraphRepository<Movie> {
    Movie findByMlId(String mlId);

    @Query("MATCH (p:Person)-[:LIKES]->(m:Movie), " +
           "(other:Person)-[:LIKES]->(m), " +
    	   "(other)-[:LIKES]->(recommendation:Movie) " +
           "WHERE NOT (p)-[:LIKES]->(recommendation) " +
    	   "AND p.userName = {0}" +
           "RETURN DISTINCT recommendation")
    Iterable<Movie> recommendedMoviesFor(String userName);

    @Query("MATCH (p:Person)-[:LIKES]->(m:Movie), " + 
           "(p)-[:LIKES]->(recommendation:Movie) " + 
    	   "WHERE m.mlId = {0} " +
           "RETURN distinct recommendation")
    Iterable<Movie> moviesLikedByPeopleWhoLiked(String mlId);
}
