package business.notification;

import business.exceptions.ClientException;
import business.mail.Mailer;
import db.models.EmailList;
import db.models.Events;

import javax.inject.Inject;
import java.util.List;

public class NotificationService {

    private Mailer mailer;
    private EmailList emailList;

    @Inject
    public NotificationService(Mailer mailer, EmailList emailList) {
        this.mailer = mailer;
        this.emailList = emailList;
    }

    public List<EmailList> getAllEmailLists() {
        return EmailList.finder.all();
    }

    public EmailList getEmailList(Long id) {
        return EmailList.finder.byId(id);
    }

    public EmailList createEmailList(String name, String description, List<String> emails) throws ClientException {

        if (emails == null) {
            throw new ClientException("emailListCanNotBeEmpty", "Email list can not be empty!");
        }

        if (emailList.findByName(name) != null) {
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

        if (emailList.findByName(name) != null) {
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

    //TODO
    public List<Events> getEvents() {
        return null;
    }

    public void sendNotification(Long eventId, List<Long> emailListIds) throws ClientException {
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
