package business.notification;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.mail.Mailer;
import business.scraper.WebScraper;
import db.models.EmailList;
import db.models.Events;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class NotificationService {

    private Mailer mailer;
    private WebScraper webScraper;

    @Inject
    public NotificationService(Mailer mailer, WebScraper webScraper) {
        this.mailer = mailer;
        this.webScraper = webScraper;
    }

    public List<EmailList> getAllEmailLists() {
        return EmailList.finder.query().orderBy("id").findList();
    }

    public EmailList getEmailList(Long id) {
        return EmailList.finder.byId(id);
    }

    public EmailList createEmailList(String name, String description, List<String> emails) throws ClientException {

        if (emails == null) {
            throw new ClientException("emailListCanNotBeEmpty", "Email list can not be empty!");
        }

        if (EmailList.findByName(name) != null) {
            throw new ClientException("emailListNameIsAlreadyExists", "Email list's name is already exists.");
        }

        EmailList emailList = new EmailList(name, description, emails);
        emailList.save();
        return emailList;
    }

    public EmailList editEmailList(Long id, String name, String description, List<String> emails) throws ClientException {

        EmailList email = EmailList.finder.byId(id);

        if (email == null) {
            throw new ClientException("emailListCouldNotFound", "There is no email list by this id.");
        }

        if (emails == null) {
            throw new ClientException("emailListCanNotBeEmpty", "Email list can not be empty!");
        }

        EmailList emailList = EmailList.findByName(name);
        if (emailList != null && !emailList.getId().equals(email.getId())) {
            throw new ClientException("emailListNameIsAlreadyExists", "Email list's name is already exists.");
        }

        email.setName(name);
        email.setDescription(description);
        email.setEmails(emails);
        email.save();
        return email;
    }

    public EmailList deleteEmailList(Long id) throws ClientException {
        EmailList emailList = EmailList.finder.byId(id);

        if (emailList == null) {
            throw new ClientException("emailListCouldNotFound", "There is no email list by this id.");
        }

        emailList.delete();
        return emailList;
    }


    public List<Events> getEvents() throws IOException, ClientException {

        List<Events> eventsList = webScraper.scrapeTopic();

        for (Events event: eventsList) {
            if (Events.finder.query().where().eq("title", event.getTitle()).findCount() == 0)
                event.save();
        }

        List<Events> events = Events.finder.all();
        if (events.isEmpty()) {
            throw new ClientException("eventCouldNotFound", "There is no event.");
        }

        return events;
    }

    public void sendNotification(Long eventId, List<Long> emailListIds) throws ClientException, ServerException {
        Events event = Events.finder.byId(eventId);

        if (event == null) {
            throw new ClientException("eventCouldNotFound", "There is no event by this id.");
        }

        for (Long id: emailListIds) {
            EmailList emailList = EmailList.finder.byId(id);
            if (emailList == null) {
                throw new ClientException("emailListCouldNotFound", "There is no email list by this id.");
            }
            mailer.sendEmail(emailList.getEmails(), event);

        }
    }
}
