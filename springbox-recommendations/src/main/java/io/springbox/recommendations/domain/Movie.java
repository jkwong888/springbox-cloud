package io.springbox.recommendations.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Movie {

    @GraphId
    private Long id;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", mlId='" + mlId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Index(unique = true)
    private String mlId;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMlId() {
        return mlId;
    }

    public void setMlId(String mlId) {
        this.mlId = mlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
