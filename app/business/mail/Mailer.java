package business.mail;

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


    public void sendToEmailList(String subject, String fromAddress, List<String> emailList, String bodyText) {
        String cid = "1234";
        Email email = new Email()
                .setSubject(subject)
                .setFrom("<" + fromAddress + ">")
                .addTo(null)
                .addAttachment("", null)
                .setBodyText(bodyText);

        for (String address: emailList) {
            email.addTo("<" + address + ">");
        }

        mailerClient.send(email);
    }
}

