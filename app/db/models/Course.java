package db.models;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Course extends Model {

    @Id
    private Long id;

    private String code;

    private String name;

    private String instructors;

    private String assistants;

    private Integer credit;

    private Boolean status;

    private Boolean labratory;

    public Course(String code, String name, String instructors, String assistants, Integer credit, Boolean status, Boolean labratory) {
        this.code = code;
        this.name = name;
        this.instructors = instructors;
        this.assistants = assistants;
        this.credit = credit;
        this.status = status;
        this.labratory = labratory;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Course setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getInstructors() {
        return instructors;
    }

    public Course setInstructors(String instructors) {
        this.instructors = instructors;
        return this;
    }

    public String getAssistants() {
        return assistants;
    }

    public Course setAssistants(String assistants) {
        this.assistants = assistants;
        return this;
    }

    public Integer getCredit() {
        return credit;
    }

    public Course setCredit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public Course setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public Boolean getLabratory() {
        return labratory;
    }

    public Course setLabratory(Boolean labratory) {
        this.labratory = labratory;
        return this;
    }
}
