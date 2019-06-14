package business.internal;

import business.handlers.DatabaseHandler;
import business.mail.Mailer;
import db.models.Events;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class ServerInitializer {

    private final EbeanConfig ebeanConfig;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;
    private final DatabaseHandler databaseHandler;
    private final Logger.ALogger logger = Logger.of(this.getClass());
    private final Mailer mailer;

    @Inject
    public ServerInitializer(EbeanConfig ebeanConfig, EbeanDynamicEvolutions ebeanDynamicEvolutions,
                             DatabaseHandler databaseHandler, Mailer mailer) {
        this.ebeanConfig = ebeanConfig;
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.databaseHandler = databaseHandler;
        this.mailer = mailer;
        initialize();
    }

    private void initialize() {
        try {
            logger.info("Initializing server...");
            // fix database state
            databaseHandler.start();
            logger.info("Server successfully initialized.");
            //Events event = new Events("hi", "hello");
            //mailer.sendEmail(Arrays.asList("muratkaryagdi@std.iyte.edu.tr", "elifduran@std.iyte.edu.tr"), event);
        } catch (Exception e) {
            logger.error("server couldn't initialized!", e);
        }
    }

}
