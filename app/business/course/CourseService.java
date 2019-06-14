package business.course;

import db.models.Course;
import db.models.WeeklyScheduleNode;

import java.util.Arrays;
import java.util.List;

public class CourseService {

    public List<WeeklyScheduleNode> getAllWeeklySchedule() {
        return WeeklyScheduleNode.finder.all();
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
