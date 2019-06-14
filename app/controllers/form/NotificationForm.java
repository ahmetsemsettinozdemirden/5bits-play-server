package controllers.form;

import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import java.util.List;

public class NotificationForm {

    @NotNull
    @Constraints.Required
    public Long eventId;

    @NotNull
    @Constraints.Required
    public List<Long> emailListIds;
}
