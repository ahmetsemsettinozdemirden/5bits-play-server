package db.repository;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import business.user.UserHelper;
import db.models.User;
import db.models.UserType;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UserRepository {

    private final UserHelper userHelper;

    @Inject
    public UserRepository(UserHelper userHelper) {
        this.userHelper = userHelper;
    }

    public List<User> getAll() {
        return User.finder.all();
    }

    public List<User> getAllContentManagers() {
        return User.finder.query().where().eq("type", UserType.CONTENT_MANAGER).findList();
    }

    public User get(Long id) {
        return User.finder.byId(id);
    }

    public User get(String email) {
        return User.finder.query()
                .where()
                .eq("email", email)
                .setMaxRows(1)
                .findOne();
    }

    public User create(String name, String email, String password, UserType type)
            throws ClientException, ServerException {

        if (email == null || email.equals(""))
            throw new ClientException("invalidEmail", "Email can not be null or empty.");

        if (password == null || password.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        if (name == null || name.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        User existedUser = get(email);

        if (existedUser != null)
            throw new ClientException("emailExists", "Email already exists. Please use a different email.");

        User user = new User(name, email, password, null, type);
        try {
            userHelper.refreshToken(user);
        } catch (UnsupportedEncodingException e) {
            throw new ServerException("refreshTokenError", e);
        }
        return user;
    }

    public User update(Long id, String email, String password, UserType type) throws ClientException {

        if (email == null || email.equals(""))
            throw new ClientException("invalidEmail", "Email can not be null or empty.");

        if (password == null || password.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        User user = get(id);

        if (user == null)
            throw new ClientException("userNotFound", "User does not exist.");

        user.setEmail(email);
        user.setPassword(password);
        user.setType(type);
        user.save();
        return user;
    }

    public boolean delete(Long id) throws ClientException {

        User user = get(id);

        if (user == null)
            throw new ClientException("userNotFound", "User could not found!");

        return user.delete();
    }

    public int getTotalUserCount(UserType type) {
        return User.finder.query()
                .where()
                .eq("type", type)
                .findCount();
    }

}
