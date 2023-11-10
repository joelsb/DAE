package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import jakarta.persistence.Id;

import java.io.Serializable;

public class AdministratorDTO implements Serializable {
    @Id
    private String username;
    private String password;
    private String name;
    private String email;

    public AdministratorDTO() {
        // Default constructor
    }

    public AdministratorDTO(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
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
    // Other properties and methods as needed
}
