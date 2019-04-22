package db.models;

import io.ebean.annotation.DbEnumValue;

public enum UserType {

    ADMIN("ADMIN"),
    CONTENT_MANAGER("CONTENT_MANAGER");

    private String value;

    /**
     * determines which operating system type to use
     * @param value type of the os
     */
    UserType(String value) {
        this.value = value;
    }

    @DbEnumValue
    public String getValue() {
        return value;
    }
}
