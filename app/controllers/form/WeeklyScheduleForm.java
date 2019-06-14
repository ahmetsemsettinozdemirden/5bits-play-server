package controllers.form;


import java.util.ArrayList;
import java.util.List;

public class WeeklyScheduleForm {

    public String section;

    public String day;

    public String hour;

    public List<String> courses;

    public WeeklyScheduleForm() {
        this.courses = new ArrayList<>();
    }
}
