package db.models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class WeeklyScheduleNode extends Model {

    @Id
    private long id;

    private String section;

    private String day;

    private String hour;

    private List<Course> courses;

    public static final Finder<Long, WeeklyScheduleNode> finder = new Finder<>(WeeklyScheduleNode.class);

    public WeeklyScheduleNode(String section, String day, String hour, List<Course> courses) {
        this.section = section;
        this.day = day;
        this.hour = hour;
        this.courses = courses;
    }

    public long getId() {
        return id;
    }

    public String getSection() {
        return section;
    }

    public WeeklyScheduleNode setSection(String section) {
        this.section = section;
        return this;
    }

    public String getDay() {
        return day;
    }

    public WeeklyScheduleNode setDay(String day) {
        this.day = day;
        return this;
    }

    public String getHour() {
        return hour;
    }

    public WeeklyScheduleNode setHour(String hour) {
        this.hour = hour;
        return this;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public WeeklyScheduleNode setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }
}
