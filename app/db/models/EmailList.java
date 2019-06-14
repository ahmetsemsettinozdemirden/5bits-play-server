package db.models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class EmailList extends Model {

    private Long id;

    private String name;

    private String description;

    private List<String> emails;

    public EmailList(String name, String description, List<String> emails) {
        this.name = name;
        this.description = description;
        this.emails = emails;
    }

    public static final Finder<Long, EmailList> finder = new Finder<>(EmailList.class);

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmailList setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EmailList setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getEmails() {
        return emails;
    }

    public EmailList setEmails(List<String> emails) {
        this.emails = emails;
        return this;
    }

    public EmailList findByName(String name) {
        return EmailList.finder.query().where().eq("name", name).findOne();
    }
}
