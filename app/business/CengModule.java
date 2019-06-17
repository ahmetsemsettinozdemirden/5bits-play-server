package business;

import akka.stream.javadsl.Source;
import business.course.CourseService;
import business.exceptions.ServerException;
import db.models.Course;
import db.models.WeeklyScheduleNode;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Http.MultipartFormData.DataPart;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CengModule {

    private CourseService courseService;
    private WSClient wsClient;
    private final String worldPressUrl = "https://public-api.wordpress.com/wp/v2/sites/ceng316group5bits.wordpress.com/pages/";
    private final String worldPressToken = "Bearer wVxCS6Q6a&X8HSRO#a$6@a!43tInrywWr92Oa%*wEKFKxpPCpBB77Mvfmr6gFPTb";

    @Inject
    public CengModule(CourseService courseService, WSClient wsClient) {
        this.courseService = courseService;
        this.wsClient = wsClient;
    }

    public String createTable() {
/*
        WeeklyScheduleNode aNode = weeklySchedule.stream().filter(node ->
                node.getSection().equals("First Year") &&
                        node.getDay().equals("Monday") &&
                        node.getHour().equals("08:45")).findAny().orElse(null);*/

        List<WeeklyScheduleNode> weeklySchedule = courseService.getAllWeeklySchedule();
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
        int nodeCounter = 0;
        for (String section : sections) {
            StringBuilder table = new StringBuilder("<table class=wp-block-table><tbody>");
            table.append("<tr><td>" + section + "</td>"+
                    "<td><strong>Monday</strong></td>" +
                    "<td><strong>Tuesday</strong></td>" +
                    "<td><strong>Wednesday</strong></td>" +
                    "<td><strong>Thursday</strong></td>" +
                    "<td><strong>Friday</strong></td>" +
                    "</tr>" );
            for (String hour : hours) {
                table.append("<tr>");
                table.append("<td><strong>" + hour + "</strong></td>");
                for (String day : days) {
                    WeeklyScheduleNode aNode = weeklySchedule.get(nodeCounter); /*weeklySchedule.stream().filter(node ->
                            node.getSection().equals(section) &&
                                    node.getDay().equals(day) &&
                                    node.getHour().equals(hour)).findAny().orElse(null);*/

                    if (aNode.getCourses() == null){
                        table.append("<td>&nbsp;</td>");
                    }
                    else{
                        table.append("<td>");
                        for(String course : aNode.getCourses())
                            table.append(course + "</br>");
                        table.append("</td>");
                    }
                    nodeCounter++;
                }
                table.append("</tr>");
            }
            table.append("</tbody></table>");
            html.append(table);
        }
        return html.toString();
    }

    private String courseCodeToUrlCode(String code) {
        return code.substring(0,4).toLowerCase() + "-" + code.substring(4, code.length());
    }

    private String createCoursesHtml() {
        StringBuilder pages = new StringBuilder();
        pages.append("<ul>");

        for (Course course : courseService.getAllCourses()) {
            pages.append("<li><a href=\"https://ceng316group5bits.wordpress.com/" +
                    courseCodeToUrlCode(course.getCode()) + "/\">" + course.getCode() + "</a></li>");
        }

        pages.append("</ul>");
        return pages.toString();
    }

    private String createCourseHtml(Course course) {

        return "<p><strong>" + course.getName() + "</strong></p>" +
                "<p><strong>Instructors:</strong></p>" +
                "<ul>" +
                "<li>" + course.getInstructors() + "</li>" +
                "</ul>" +
                "<p><strong>Assistants:" + course.getAssistants() + "</strong></p>" +
                "<p><strong>Status:</strong> " + (course.getStatus() ? "Offered" : "Pending") + "</p>";
    }

    private void sendEditCourseRequest(Course course) throws ExecutionException, InterruptedException {
        String url = worldPressUrl + course.getWordPressId();
        if (course.getWordPressId() == null) {
            url = worldPressUrl;
        }
        String body = wsClient.url(url)
                .addHeader("Authorization", worldPressToken)
                .post(Source.from(Arrays.asList(new DataPart("content", createCourseHtml(course)),
                        new DataPart("status", "publish"),
                        new DataPart("title", course.getCode()),
                        new DataPart("slug", ""))))
                .toCompletableFuture()
                .get().getBody();

        course.setWordPressId(Json.parse(body).get("id").asInt());
        course.setEdited(false);
        course.save();
    }

    private void sendDeleteCourseRequest(Course course) throws ExecutionException, InterruptedException {
        String url = worldPressUrl + course.getWordPressId();
        wsClient.url(url)
                .addHeader("Authorization", worldPressToken)
                .delete()
                .toCompletableFuture()
                .get().getBody();
        course.delete();
    }

    public void publishWeeklySchedules() throws ServerException {
        try {
            String body = wsClient.url(worldPressUrl + "16") // 16 is id of weekly schedules page
                    .addHeader("Authorization", worldPressToken)
                    .post(Source.from(Arrays.asList(new DataPart("content", createTable()),
                                                    new DataPart("status", "publish"),
                                                    new DataPart("title", "Weekly Course Schedules"),
                                                    new DataPart("slug", ""))))
                    .toCompletableFuture()
                    .get().getBody();
            Logger.error(body);
        } catch (InterruptedException | ExecutionException e) {
            throw new ServerException("wordPressError", e);
        }

        for (Course course : courseService.getAllCourses()) {
            try {
                if (course.getDeleted()) {
                    sendDeleteCourseRequest(course);
                } else if (course.getEdited()) {
                    sendEditCourseRequest(course);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new ServerException("wordPressError", e);
            }
        }

        try {
            String body = wsClient.url(worldPressUrl + "18") // 18 is id of courses page
                    .addHeader("Authorization", worldPressToken)
                    .post(Source.from(Arrays.asList(new DataPart("content", createCoursesHtml()),
                            new DataPart("status", "publish"),
                            new DataPart("title", "Courses"),
                            new DataPart("slug", ""))))
                    .toCompletableFuture()
                    .get().getBody();
            Logger.error(body);
        } catch (InterruptedException | ExecutionException e) {
            throw new ServerException("wordPressError", e);
        }
    }
}
