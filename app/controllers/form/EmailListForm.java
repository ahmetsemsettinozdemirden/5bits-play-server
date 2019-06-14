package controllers.form;

import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import java.util.List;

public class EmailListForm {

    @NotNull
    @Constraints.Required
    public String name;

    public String description;

    @NotNull
    @Constraints.Required
    public List<String> emails;
}
