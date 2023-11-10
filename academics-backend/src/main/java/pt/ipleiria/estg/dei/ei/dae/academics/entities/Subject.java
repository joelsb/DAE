package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Table(
        name = "subjects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_subject_constraint",
                        columnNames = {"name", "course_code", "scholar_year"}
                )
        }
)
@NamedQueries({
        @NamedQuery(
                name = "getAllSubjects",
                query = "SELECT s FROM Subject s ORDER BY s.course.name, s.scholarYear DESC, s.courseYear, s.name" // JPQL
        )
})
@Entity
public class Subject {
    @Id
    private long code;
    @NotNull
    private String name;
    @NotNull
    @ManyToOne
    private Course course;
    @NotNull
    @Column(name="course_year")
    private int courseYear;
    @NotNull
    @Column(name="scholar_year")
    private int scholarYear;
    @NotNull
    @ManyToMany
    @JoinTable(
            name = "subjects_students",
            joinColumns = @JoinColumn(
                    name = "subject_code",
                    referencedColumnName = "code"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "student_username",
                    referencedColumnName = "username"
            )
    )
    private List<Student> students;

    @NotNull
    @ManyToMany(mappedBy = "subjects")
    private List<Teacher> teachers;

    public Subject() {
        students = new ArrayList<>();
    }
    public Subject(long code, String name, Course course, int courseYear, int scholarYear) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public int getScholarYear() {
        return scholarYear;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
    }

    public void setScholarYear(int scholarYear) {
        this.scholarYear = scholarYear;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    public void removeStudent(Student student) {
        students.remove(student);
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }
}
