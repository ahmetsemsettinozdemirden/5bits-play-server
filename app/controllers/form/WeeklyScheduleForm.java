package controllers.form;

import db.models.Course;

import java.util.ArrayList;
import java.util.List;

public class WeeklyScheduleForm {

    public String section;

    public String day;

    public String hour;

    public List<Course> courses;

    public WeeklyScheduleForm() {
        this.courses = new ArrayList<>();
    }
}
