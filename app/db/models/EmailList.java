package db.models;
import io.ebean.annotation.NotNull;
import play.libs.mailer.Email;

import java.util.List;

public class EmailList {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private List<Email> emailList;

    public EmailList(String name, String description, List<Email> emailList) {
        this.name = name;
        this.description = description;
        this.emailList = emailList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<Email> emailList) {
        this.emailList = emailList;
    }
}
