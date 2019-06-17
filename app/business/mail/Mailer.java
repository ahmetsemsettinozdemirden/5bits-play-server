package business.mail;

import db.models.Events;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import java.util.List;


public class Mailer {

    private MailerClient mailerClient;

    @Inject
    public Mailer(MailerClient mailerClient) {
        this.mailerClient = mailerClient;
    }

    public void sendEmail(List<String> emailList, Events event) {
        Email email = new Email()
                .setSubject(event.getTitle())
                .setFrom("5BitsViewer <elifduran@std.iyte.edu.tr>")
                .setBodyText(event.getBody());

        for (String address: emailList) {
            email.addTo("<" + address + ">");
        }

        mailerClient.send(email);
    }
}

