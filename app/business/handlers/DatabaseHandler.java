package business.handlers;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.notification.NotificationService;
import db.models.*;
import db.repository.UserRepository;
import play.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public DatabaseHandler(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void start() {
        logger.info("Database Handler starting...");
        createDefaultAdmin();
        createDefaultContentManager();
        createDefaultEmailLists();
        createDefaultEvents();
        createDefaultWeeklyScheduleNode();
        logger.info("Database Handler successfully completed.");
    }

    private void createDefaultAdmin() {
        if (userRepository.getTotalUserCount(UserType.ADMIN) == 0) {
            try {
                userRepository.create("jesus", "test@email.com", "password", UserType.ADMIN).save();
                logger.info("test admin inserted.");
            } catch (ClientException | ServerException e) {
                logger.error("create default admin error.", e);
            }
        }
    }

    private void createDefaultContentManager() {
        if (userRepository.getTotalUserCount(UserType.CONTENT_MANAGER) == 0) {
            try {
                userRepository.create("jesus", "content@email.com", "password", UserType.CONTENT_MANAGER).save();
                logger.info("test content manager inserted.");
            } catch (ClientException | ServerException e) {
                logger.error("create default content manager error.", e);
            }
        }
    }

    private void createDefaultEmailLists() {
        if (EmailList.finder.all().isEmpty()) {
            try {
                List<String> emailList1 = Arrays.asList("elifduran", "muratkaryagdi");
                notificationService.createEmailList("emailList1", "desc", emailList1);

                List<String> emailList2 = Arrays.asList("mberkayozkan", "huseyinberkgok");
                notificationService.createEmailList("emailList2", "desc", emailList2);
            } catch (ClientException e) {
                logger.error("create default email lists error.", e);
            }
        }
    }

    private void createDefaultEvents() {
        if (Events.finder.all().isEmpty()) {

            new Events("title1", "body1").save();
            new Events("title2", "body2").save();
        }
    }

    private void createDefaultWeeklyScheduleNode() {
        if (WeeklyScheduleNode.finder.query().setMaxRows(1).findOne() == null) {
            logger.info("inserting weekly schedule nodes.");
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
            for (String section : sections) {
                for (String day : days) {
                    for (String hour : hours) {
                        new WeeklyScheduleNode(section, day, hour, null).save();
                    }
                }
            }
            logger.info("weekly schedule nodes inserted ...");
        }
    }

    private void createDefaultCourseList() {
        if (Course.finder.all().isEmpty()) {

            new Course("CENG 111", "Concepts in Computer Engineering", "Yusuf Murat Erten",
                    "Didem Genç, Orhan Bayraktar, Samet Tenekeci", 3, true, false).save();
            new Course("CENG 113", "Programming Basics", "Nesli Erdoğmuş",
                    "Büşra Güvenoğlu, Ersin Çine, Leyla Tekin, Samet Tenekeci", 3, true, true).save();
            new Course("CENG 114", "Probability & Statistics", "Nesli Erdoğmuş",
                    "Damla Yaşar", 3, true, false).save();
            new Course("CENG 115", "Concepts in Computer Engineering", "Yusuf Murat Erten",
                    "Didem Genç, Orhan Bayraktar, Samet Tenekeci", 3, true, false).save();
            new Course("CENG 211", "Programming Fundamentals", "Tuğkan Tuğlular",
                    "Deniz Kavzak Ufuktepe, Dilek Öztürk, Ekincan Ufuktepe", 3, true, false).save();
            new Course("CENG 212", "Concepts of Programming Languages", "Selma Tekir",
                    "Damla Yaşar, Erhan Sezer, Ozan Polatbilek", 3, false, false).save();
        }
    }
}
