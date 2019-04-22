package business.user;

import business.jwt.JwtHelper;
import db.models.User;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class UserHelper {

    private final JwtHelper jwtHelper;

    @Inject
    public UserHelper(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public void refreshToken(User user) throws UnsupportedEncodingException {
        user.setToken(jwtHelper.getSignedToken(user.getEmail()));
    }

}
