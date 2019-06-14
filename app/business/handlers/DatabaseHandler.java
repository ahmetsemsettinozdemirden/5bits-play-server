package business.handlers;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import db.models.UserType;
import db.repository.UserRepository;
import play.Logger;

import javax.inject.Inject;

public class DatabaseHandler {

    private final UserRepository userRepository;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public DatabaseHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void start() {
        logger.info("Database Handler starting...");
        createDefaultAdmin();
        createDefaultContentManager();
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

}
