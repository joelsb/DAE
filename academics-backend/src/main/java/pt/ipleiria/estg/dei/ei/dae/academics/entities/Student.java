package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Table(name="students")
@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT s FROM Student s ORDER BY s.name" // JPQL
        )
})
public class Student extends User implements Serializable {
    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    private Course course;

    @NotNull
    @ManyToMany(mappedBy = "students")
    private List<Subject> subjects;


    public Student() {
        super();
    }
    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        course.addStudent(this);
        this.subjects = new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }
    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }
}
