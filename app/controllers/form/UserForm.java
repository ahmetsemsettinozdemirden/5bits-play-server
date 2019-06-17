package controllers.form;

import play.data.validation.Constraints;

public class UserForm {

    public String name;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

}
