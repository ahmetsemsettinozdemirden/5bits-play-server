package business.course;

import business.exceptions.ClientException;
import db.models.Course;
import db.models.WeeklyScheduleNode;

import java.util.List;

public class CourseService {

    public List<WeeklyScheduleNode> getAllWeeklySchedule() {
        return WeeklyScheduleNode.finder.query().orderBy("id asc").findList();
    }

    public Course addCourse(String code, String name, String instructors, String assistants, Integer credit, Boolean status,
                            Boolean laboratory) throws ClientException {
        Course oldCourse = Course.finder.query().where().eq("code", code).findOne();
        if (oldCourse != null && !oldCourse.getDeleted()) {
            throw new ClientException("courseExist", "This course already created.");
        } else if (oldCourse != null && oldCourse.getDeleted()) {
            return editCourse(code, code,  name, instructors, assistants, credit, status, laboratory);
        }
        Course course = new Course(code, name, instructors, assistants, credit, status, laboratory, true, false, null);
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
        course.setEdited(true);
        course.setDeleted(false);
        course.save();
        return course;
    }

    public List<Course> getAllCourses() {
        return Course.finder.query()
                .where()
                .eq("deleted", false)
                .orderBy("id")
                .findList();
    }

    public List<Course> getOfferedCourses() {
        return Course.finder.query()
                .where()
                .eq("status", true)
                .eq("deleted", false)
                .orderBy("id")
                .findList();
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
        if (course.getDeleted()) {
            throw new ClientException("courseAlreadyDeleted", "This course is already deleted.");
        }
        if (course.getWordPressId() == null) {
            course.delete();
        } else {
            course.setDeleted(true);
            course.save();
        }
    }

    public WeeklyScheduleNode updateWeeklyScheduleNode(String section, String day, String hour, List<String> courses) throws ClientException {
        for (String code : courses) {
            Course course = getCourse(code);
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


}
