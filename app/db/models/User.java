package db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.Index;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.UpdatedTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="usr")
@Index(columnNames = { "type" })
public class User extends Model {

    @Id
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String token;

    @NotNull
    private UserType type;

    @CreatedTimestamp
    private LocalDateTime createdAt;

    @UpdatedTimestamp
    private LocalDateTime updatedAt;

    public static final Finder<Long, User> finder = new Finder<>(User.class);

    public User(String email, String password, String token, UserType type) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @JsonIgnore
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonIgnore
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}