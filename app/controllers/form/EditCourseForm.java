package controllers.form;

import play.data.validation.Constraints;

public class EditCourseForm {

    @Constraints.Required
    public String codeToEdit;

    @Constraints.Required
    public String code;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String instructors;

    @Constraints.Required
    public String assistants;

    @Constraints.Required
    public Integer credit;

    @Constraints.Required
    public Boolean status;

    public Boolean laboratory;
}
