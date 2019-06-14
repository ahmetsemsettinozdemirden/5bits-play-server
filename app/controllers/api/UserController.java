package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.form.UserForm;
import db.models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller {

    private FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }
    @BodyParser.Of(BodyParser.Json.class)
    public Result login() {
        Form<UserForm> form = formFactory.form(UserForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        UserForm body = form.get();

        User user = User.finder.query().where().eq("email", body.email).findOne();

        if (user == null) {
            return badRequest("there is no such user");
        }

        if (!user.getPassword().equals(body.password)) {
            return badRequest("password is invalid");
        }

        ObjectNode result = Json.newObject();
        result.put("accessToken", user.getToken());
        return ok(result);
    }
}
