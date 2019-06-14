package controllers.form;

import play.data.validation.Constraints;

public class PasswordForm {

    @Constraints.Required
    public String password;
}
