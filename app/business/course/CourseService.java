package business.course;

import db.models.WeeklyScheduleNode;

import java.util.List;

public class CourseService {

    public List<WeeklyScheduleNode> getAllWeeklySchedule() {
        return WeeklyScheduleNode.finder.all();
    }
}
