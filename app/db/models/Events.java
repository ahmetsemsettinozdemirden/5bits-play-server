package db.models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Events extends Model {

    @Id
    private Long id;

    private String title;

    @Column(length = 2048)
    private String body;

    public Events(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public static final Finder<Long, Events> finder = new Finder<>(Events.class);

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

