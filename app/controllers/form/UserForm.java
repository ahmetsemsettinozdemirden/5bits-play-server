package controllers.form;

import play.data.validation.Constraints;

public class UserForm {

    public String name;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

}
