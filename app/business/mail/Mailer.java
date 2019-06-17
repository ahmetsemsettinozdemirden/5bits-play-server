package business.mail;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import db.models.Events;
import org.apache.commons.mail.EmailException;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import java.util.List;


public class Mailer {

    private MailerClient mailerClient;

    @Inject
    public Mailer(MailerClient mailerClient) {
        this.mailerClient = mailerClient;
    }

    public void sendEmail(List<String> emailList, Events event) throws ClientException, ServerException {
        Email email = new Email()
                .setSubject(event.getTitle())
                .setFrom("5BitsViewer <elifduran@std.iyte.edu.tr>")
                .setBodyText(event.getBody());

        for (String address: emailList) {
            email.addTo("<" + address + ">");
        }
        try {
            mailerClient.send(email);
        }catch (Exception e) {
            if (e.getClass().equals(EmailException.class)) {
                throw new ClientException("InvalidMail", e.getLocalizedMessage());
            } else {
                throw new ServerException("MailSendError", e);
            }
        }
    }
}

