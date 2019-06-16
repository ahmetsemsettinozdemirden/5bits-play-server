package controllers.form;

import play.data.validation.Constraints;

public class ContentManagerForm {

    @Constraints.Required
    public String email;
}
