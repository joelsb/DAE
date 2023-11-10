package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Table(name = "teachers")
@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTeachers",
                query = "SELECT t FROM Teacher t ORDER BY t.name" // JPQL
        )
})
public class Teacher extends User implements Serializable {
    @NotNull
    private String office;

    @ManyToMany
    @JoinTable(
            name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code")
    )
    private List<Subject> subjects;

    public Teacher() {
        // Default constructor
        super();
    }

    public Teacher(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
        subjects = new ArrayList<>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.addTeacher(this);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
        subject.removeTeacher(this);
    }
}
