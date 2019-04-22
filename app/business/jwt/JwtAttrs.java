package business.jwt;

import db.models.User;
import play.libs.typedmap.TypedKey;

public class JwtAttrs {
    public static final TypedKey<User> VERIFIED_USER = TypedKey.create("user");
}
