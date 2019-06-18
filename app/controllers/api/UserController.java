package controllers.api;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.jwt.JwtAttrs;
import business.mail.Mailer;
import business.user.UserHelper;
import business.user.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.form.ContentManagerForm;
import controllers.form.PasswordForm;
import controllers.form.UserForm;
import db.models.Events;
import db.models.User;
import db.models.UserType;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Arrays;

public class UserController extends Controller {

    private FormFactory formFactory;
    private UserHelper userHelper;
    private UserService userService;
    private Mailer mailer;

    @Inject
    public UserController(FormFactory formFactory, UserHelper userHelper, UserService userService, Mailer mailer) {
        this.formFactory = formFactory;
        this.userHelper = userHelper;
        this.userService = userService;
        this.mailer = mailer;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result login() {
        Form<UserForm> form = formFactory.form(UserForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        UserForm body = form.get();
        User user;
        try {
             user = userService.signIn(body.email, body.password);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }

        ObjectNode result = Json.newObject();
        result.put("accessToken", user.getToken());
        return ok(result);
    }

    public Result logout() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);
        try {
            userService.signOut(user);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }

        return ok();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addContentManager() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);

        if (!user.getType().equals(UserType.ADMIN)) {
            return badRequest("Only admin can add content manager");
        }

        Form<UserForm> form = formFactory.form(UserForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        UserForm body = form.get();
        try {
            userService.createUser(body.name, body.email, body.password, UserType.CONTENT_MANAGER);
            Events event = new Events("Assigned as a new content manager.", "Password: " + body.password);
            mailer.sendEmail(Arrays.asList(body.email), event);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }

        return ok();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result removeContentManager() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);

        if (!user.getType().equals(UserType.ADMIN)) {
            return badRequest("Only admin can remove content manager");
        }

        Form<ContentManagerForm> form = formFactory.form(ContentManagerForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        ContentManagerForm body = form.get();

        User contentManager = User.finder.query().where()
                .eq("email", body.email)
                .eq("type", UserType.CONTENT_MANAGER)
                .findOne();
        if (contentManager == null) {
            return badRequest("there is no content manager by this email");
        }
        
        contentManager.delete();

        return ok();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updatePassword() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);

        Form<PasswordForm> form = formFactory.form(PasswordForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        PasswordForm body = form.get();

        try {
            userService.updatePassword(user, body.password);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }
        return ok();
    }


    public Result getContentManagers() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);

        if (!user.getType().equals(UserType.ADMIN)) {
            return badRequest("Only admin can add content manager");
        }

        return ok(userService.getContentManagers());
    }
}
