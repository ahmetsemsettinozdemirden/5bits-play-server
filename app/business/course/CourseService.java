package business.course;

import business.exceptions.ClientException;
import db.models.Course;
import db.models.WeeklyScheduleNode;

import java.util.Arrays;
import java.util.List;

public class CourseService {

    public List<WeeklyScheduleNode> getAllWeeklySchedule() {
        return WeeklyScheduleNode.finder.all();
    }

    public Course addCourse(String code, String name, String instructors, String assistants, Integer credit, Boolean status,
                            Boolean laboratory) throws ClientException {
        if (Course.finder.query().where().eq("code", code).findOne() != null) {
            throw new ClientException("courseExist", "This course already created.");
        }
        Course course = new Course(code, name, instructors, assistants, credit, status, laboratory);
        course.save();
        return course;
    }

    public Course editCourse(String oldCode, String code, String name, String instructors, String assistants, Integer credit, Boolean status,
                            Boolean laboratory) throws ClientException {
        Course course = Course.finder.query().where().eq("code", oldCode).findOne();
        if (course == null) {
            throw new ClientException("courseNotExist", "There is no course by this code.");
        }

        if (!oldCode.equals(code) && Course.finder.query().where().eq("code", code).findOne() != null) {
            throw new ClientException("courseExist", "This course already created.");
        }
        course.setCode(code);
        course.setName(name);
        course.setInstructors(instructors);
        course.setAssistants(assistants);
        course.setCredit(credit);
        course.setStatus(status);
        course.setLaboratory(laboratory);
        course.save();
        return course;
    }

    public List<Course> getAllCourses() {
        return Course.finder.all();
    }

    public List<Course> getOfferedCourses() {
        return Course.finder.query().where().eq("status", true).findList();
    }

    public Course getCourse(String code) throws ClientException {
        Course course = Course.finder.query().where().eq("code", code).findOne();
        if (course == null) {
            throw new ClientException("courseNotExist", "There is no course by this code.");
        }
        return course;
    }

    public void deleteCourse(String code) throws ClientException {
        Course course = Course.finder.query().where().eq("code", code).findOne();
        if (course == null) {
            throw new ClientException("courseNotExist", "There is no course by this code.");
        }
        course.delete();
    }

    public WeeklyScheduleNode updateWeeklyScheduleNode(String section, String day, String hour, List<Course> courses) throws ClientException {
        for (Course course : courses) {
            if (!course.getStatus()) {
                throw new ClientException("courseNotOffered", course.getCode() + " course is not offered.");
            }
        }

        WeeklyScheduleNode weeklyScheduleNode = WeeklyScheduleNode.finder.query().where()
                .eq("section", section)
                .eq("day", day)
                .eq("hour", hour)
                .findOne();

        if (weeklyScheduleNode == null) {
            throw new ClientException("nodeNotFound", "There is no such WeeklyScheduleNode.");
        }
        weeklyScheduleNode.setCourses(courses);
        weeklyScheduleNode.save();
        return weeklyScheduleNode;
    }

    public String createTable(List<WeeklyScheduleNode> weeklySchedule) {
/*
        WeeklyScheduleNode aNode = weeklySchedule.stream().filter(node ->
                node.getSection().equals("First Year") &&
                        node.getDay().equals("Monday") &&
                        node.getHour().equals("08:45")).findAny().orElse(null);*/


        List<String> sections = Arrays.asList("First Year", "Second Year", "Third Year", "Fourth Year", "Graduate");
        List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        List<String> hours = Arrays.asList(
                "08:45 - 09:30",
                "09:45 - 10:30",
                "10:45 - 11:30",
                "11:45 - 12:30",
                "13:30 - 14:15",
                "14:30 - 15:15",
                "15:30 - 16:15",
                "16:30 - 17:15");

        StringBuilder html = new StringBuilder();
        for (String section : sections) {
            StringBuilder table = new StringBuilder("<table class=wp-block-table><tbody>");
            table.append("<tr>" + section +
                    "<td><strong>Monday</strong></td>\n" +
                    "<td><strong>Tuesday</strong></td>\n" +
                    "<td><strong>Wednesday</strong></td>\n" +
                    "<td><strong>Thursday</strong></td>\n" +
                    "<td><strong>Friday</strong></td>\n" +
                    "</tr>" );
            for (String day : days) {
                for (String hour : hours) {
                    table.append("<tr>");
                    table.append("<td><strong>" + hour + "</strong></td>\"");
                    WeeklyScheduleNode aNode = weeklySchedule.stream().filter(node ->
                            node.getSection().equals(section) &&
                                    node.getDay().equals(day) &&
                                    node.getHour().equals(hour)).findAny().orElse(null);
                    if (aNode.getCourses().isEmpty()){
                        table.append("<td>&nbsp;</td>");
                    }
                    else{
                        table.append("<td>");
                        for(Course course : aNode.getCourses())
                            table.append(course + "</br>");
                        table.append("</td>");
                    }
                }
            }
            table.append("</tbody></table>");
            html.append(table);
        }
        return html.toString();
    }
}
