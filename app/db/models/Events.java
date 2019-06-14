package db.models;

import io.ebean.Model;

import javax.persistence.Entity;

@Entity
public class Events extends Model {

    private Long id;

    private String title;

    private String body;

    public Events(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Events setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Events setBody(String body) {
        this.body = body;
        return this;
    }
}

