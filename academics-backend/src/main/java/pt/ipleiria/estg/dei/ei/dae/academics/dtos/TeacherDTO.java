package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import jakarta.persistence.Id;

import java.io.Serializable;

public class TeacherDTO implements Serializable {
    @Id
    private String username;
    private String password;
    private String name;
    private String email;
    private String office;

    // Constructors, getters, and setters

    public TeacherDTO() {
        // Default constructor
    }

    public TeacherDTO(String username, String password, String name, String email, String office) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.office = office;
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

    // Getters and setters for other properties
        public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    // Other properties and methods as needed
}
