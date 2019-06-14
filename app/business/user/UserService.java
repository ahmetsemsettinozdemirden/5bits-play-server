package business.user;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import db.models.User;
import db.models.UserType;
import db.repository.UserRepository;

import javax.inject.Inject;

public class UserService {

    private final UserRepository userRepository;
    private final UserHelper userHelper;

    @Inject
    public UserService(UserRepository userRepository, UserHelper userHelper) {
        this.userRepository = userRepository;
        this.userHelper = userHelper;
    }

    public User signIn(String email, String password) throws ClientException, ServerException {

        User user = userRepository.get(email);

        if (user == null)
            throw new ClientException("userCouldNotFound", "User account couldn't found with email: '" + email + "'.");

        if (!user.getPassword().equals(password))
            throw new ClientException("passwordDoesNotMatch", "Password does not match!");

        try {
            userHelper.refreshToken(user);
            user.save();
        } catch (Exception e) {
            throw new ServerException("refreshTokenError", e);
        }

        return user;
    }

    public User signOut(User user) throws ClientException, ServerException {

        try {
            user.setToken(null);
            user.save();
        } catch (Exception e) {
            throw new ServerException("removeTokenError", e);
        }
        return user;
    }

    public User createUser(String email, String password, UserType type) throws ClientException, ServerException {
        User user = userRepository.create(email, password, type);
        user.save();
        return user;
    }

}
