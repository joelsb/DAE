package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.io.Serializable;
import java.util.List;

public class SubjectDTO implements Serializable {
    @Id
    private long code;
    private String name;
    private long courseCode;
    private String courseName;
    private int courseYear;
    private int scholarYear;
    public SubjectDTO() {
    }

    public SubjectDTO(long code, String name, long courseCode, String courseName, int courseYear, int scholarYear) {
        this.code = code;
        this.name = name;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public long getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public int getScholarYear() {
        return scholarYear;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseCode(long courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
    }

    public void setScholarYear(int scholarYear) {
        this.scholarYear = scholarYear;
    }
}
