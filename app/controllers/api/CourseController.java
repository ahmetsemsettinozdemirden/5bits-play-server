package controllers.api;

import business.CengModule;
import business.course.CourseService;
import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.jwt.JwtAttrs;
import com.google.inject.Inject;
import controllers.form.AddCourseForm;
import controllers.form.EditCourseForm;
import controllers.form.WeeklyScheduleForm;
import db.models.Course;
import db.models.User;
import db.models.UserType;
import db.models.WeeklyScheduleNode;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class CourseController extends Controller {

    private FormFactory formFactory;
    private CourseService courseService;
    private CengModule cengModule;

    @Inject
    public CourseController(FormFactory formFactory, CourseService courseService, CengModule cengModule) {
        this.formFactory = formFactory;
        this.courseService = courseService;
        this.cengModule = cengModule;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addCourse() {

        Form<AddCourseForm> form = formFactory.form(AddCourseForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        AddCourseForm body = form.get();
        Course course;
        try {
            course = courseService.addCourse(body.code, body.name, body.instructors, body.assistants, body.credit,
                    body.status, body.laboratory);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(course));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result editCourse() {

        Form<EditCourseForm> form = formFactory.form(EditCourseForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        EditCourseForm body = form.get();
        Course course;
        try {
            course = courseService.editCourse(body.codeToEdit, body.code, body.name, body.instructors, body.assistants, body.credit,
                    body.status, body.laboratory);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }

        return ok(Json.toJson(course));
    }

    public Result getAllCourses() {
        return ok(Json.toJson(courseService.getAllCourses()));
    }

    public Result getOfferedCourses() {
        return ok(Json.toJson(courseService.getOfferedCourses()));
    }

    public Result getCourse(String code) {
        Course course;
        try {
            course = courseService.getCourse(code);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }
        return ok(Json.toJson(course));
    }

    public Result deleteCourse(String code) {
        try {
            courseService.deleteCourse(code);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }
        return ok();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateWeeklyScheduleNode() {

        Form<WeeklyScheduleForm> form = formFactory.form(WeeklyScheduleForm.class).bind(request().body().asJson());

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        WeeklyScheduleForm body = form.get();
        WeeklyScheduleNode weeklyScheduleNode;
        try {
            weeklyScheduleNode = courseService.updateWeeklyScheduleNode(body.section, body.day, body.hour, body.courses);
        } catch (ClientException e) {
            return badRequest(e.getMessage());
        }
        return ok(Json.toJson(weeklyScheduleNode));
    }

    public Result getAllWeeklySchedule() {
        return ok(Json.toJson(courseService.getAllWeeklySchedule()));
    }

    public Result publishWeeklySchedules() {

        User user = request().attrs().get(JwtAttrs.VERIFIED_USER);

        if (!user.getType().equals(UserType.ADMIN)) {
            return badRequest("Only admin can publish Weekly Schedule and Courses.");
        }

        try {
            cengModule.publishWeeklySchedules();
        } catch (ServerException e) {
            return internalServerError(e.getMessage());
        }
        return ok();
    }

}
