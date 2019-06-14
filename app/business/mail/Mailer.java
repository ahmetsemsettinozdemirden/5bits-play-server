package business.mail;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;


public class Mailer {

    private MailerClient mailerClient;

    @Inject
    public Mailer(MailerClient mailerClient) {
        this.mailerClient = mailerClient;
    }

    public void sendEmail(String subject, String fromAddress, String toAddress, String bodyText) {
        String cid = "1234";
        Email email = new Email()
                .setSubject(subject)
                .setFrom("<" + fromAddress + ">")
                .addTo("<" + toAddress + ">")
                .addAttachment("", null)
                .setBodyText(bodyText);
        mailerClient.send(email);
    }

}

