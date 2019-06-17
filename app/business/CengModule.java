package business;

import akka.stream.javadsl.Source;
import play.Logger;
import play.libs.ws.WSResponse;
import play.mvc.Http.MultipartFormData.*;
import business.course.CourseService;
import db.models.Course;
import db.models.WeeklyScheduleNode;
import play.libs.ws.WSRequest;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.shaded.ahc.io.netty.util.concurrent.CompleteFuture;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CengModule {

    private CourseService courseService;
    private WSClient wsClient;

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
        System.out.println(html.toString());
        return html.toString();
    }

    public void pulicWeeklySchedule() {
        int page_id = 11;

        try {
            String body = wsClient.url("https://public-api.wordpress.com/wp/v2/sites/ceng316group5bits.wordpress.com/pages/16")
                    .addHeader("Authorization", "Bearer wVxCS6Q6a&X8HSRO#a$6@a!43tInrywWr92Oa%*wEKFKxpPCpBB77Mvfmr6gFPTb")

                    .post(Source.from(Arrays.asList(new DataPart("content", createTable()),
                                                    new DataPart("status", "publish"),
                                                    new DataPart("title", "weekly course schedules"),
                                                    new DataPart("slug", ""))))
                    .toCompletableFuture()
                    .get().getBody();
            Logger.error(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Logger.error(createTable());

    }
}
