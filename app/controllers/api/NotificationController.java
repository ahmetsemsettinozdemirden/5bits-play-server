package controllers.api;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.notification.NotificationService;
import controllers.form.EmailListForm;
import controllers.form.NotificationForm;
import db.models.EmailList;
import db.models.Events;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class NotificationController extends Controller {

    private NotificationService notfService;
    private FormFactory formFactory;

    @Inject
    public NotificationController(NotificationService notfService, FormFactory formFactory) {
        this.notfService = notfService;
        this.formFactory = formFactory;
    }

    public Result getAllEmailLists() {
        List<EmailList> emailList = notfService.getAllEmailLists();
        return ok(Json.toJson(emailList));
    }

    public Result getEmailList(Long id) {
        EmailList emailList = notfService.getEmailList(id);
        return ok(Json.toJson(emailList));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result createEmailList() {
        Form<EmailListForm> form = formFactory.form(EmailListForm.class).bind(request().body().asJson());

        if (form.hasErrors())
            return badRequest(form.errorsAsJson());

        EmailListForm body = form.get();
        Result result = validateEmails(body);
        if (result != null) {
            return result;
        }
        EmailList emailList;
        try {
            emailList = notfService.createEmailList(body.name, body.description, body.emails);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(emailList));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result editEmailList(Long id) {
        Form<EmailListForm> form = formFactory.form(EmailListForm.class).bind(request().body().asJson());

        if (form.hasErrors())
            return badRequest(form.errorsAsJson());

        EmailListForm body = form.get();
        Result result = validateEmails(body);
        if (result != null) {
            return result;
        }
        EmailList emailList;
        try {
            emailList = notfService.editEmailList(id, body.name, body.description, body.emails);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(emailList));
    }

    public Result deleteEmailList(Long id) {

        EmailList emailList;
        try {
            emailList = notfService.deleteEmailList(id);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(emailList));
    }

    public Result getEvents() {

        List<Events> events;
        try {
            events = notfService.getEvents();
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (IOException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(events));

    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result sendNotification() {
        Form<NotificationForm> form = formFactory.form(NotificationForm.class).bind(request().body().asJson());

        if (form.hasErrors())
            return badRequest(form.errorsAsJson());

        NotificationForm body = form.get();
        try {
            notfService.sendNotification(body.eventId, body.emailListIds);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }

        return ok();
    }

    private Result validateEmails(EmailListForm emailListForm) {
        for (String email : emailListForm.emails) {
            if (!email.contains("@")) {
                return badRequest("invalid eMail: " + email);
            }
        }
        return null;
    }

}
