package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import jakarta.persistence.Id;

import java.io.Serializable;


//DTO - Data Transfer Object (JSON-Object)
public class StudentDTO implements Serializable {
    //same properties as the Student Entity
    @Id
    private String username;
    private String password;
    private String name;
    private String email;
    private long courseCode;
    private String courseName;

    public StudentDTO() {
    }
    public StudentDTO(String username, String password, String name, String email, long courseCode, String courseName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public long getCourseCode() {
        return courseCode;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseCode(long courseCode) {
        this.courseCode = courseCode;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
