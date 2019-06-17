package controllers.form;

import play.data.validation.Constraints;

public class AddCourseForm {

    @Constraints.Required
    public String code;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String instructors;

    public String assistants;

    @Constraints.Required
    public Integer credit;

    @Constraints.Required
    public Boolean status;

    @Constraints.Required
    public Boolean laboratory;
}
