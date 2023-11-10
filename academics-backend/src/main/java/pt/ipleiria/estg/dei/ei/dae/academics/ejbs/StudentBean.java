package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager entityManager;
    public void create(String username, String password, String name, String email, long courseCode) throws MyEntityExistsException, MyEntityNotFoundException {
        var student = entityManager.find(Student.class, username);
        if(student != null){
            throw new MyEntityExistsException("Student with username: " + username + " already exists");
        }
        Course course = entityManager.find(Course.class, courseCode);
        if (course == null) {
            throw new MyEntityNotFoundException("Course with code: " + courseCode + " not found");
        }
        var newStudent = new Student(username, password, name, email, course);
        entityManager.persist(newStudent);
    }
    public List<Student> getAll() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return entityManager.createNamedQuery("getAllStudents", Student.class).getResultList();
    }
    public Student find(String username) {
        return entityManager.find(Student.class, username);
    }
    public Student findWithSubjects(String username) {
        var student = this.find(username);
        Hibernate.initialize(student.getSubjects());
        return student;
    }
    public void update(String username, String password, String name, String email, long courseCode) {
        Student student = entityManager.find(Student.class, username);
        if (student == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + username);
            return;
        }
        entityManager.lock(student, LockModeType.OPTIMISTIC);
        student.setPassword(password);
        student.setName(name);
        student.setEmail(email);
        if (student.getCourse().getCode() != courseCode) {
            Course course = entityManager.find(Course.class, courseCode);
            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }
            student.setCourse(course);
        }
    }

    public void enrollStudentInSubject(String username, long subjectCode) {
        Student student = this.find(username);
        Subject subject = entityManager.find(Subject.class, subjectCode);

        if (student == null){
            System.out.println("Student not found");
            return;
        }
        if (subject == null){
            System.out.println("Subject not found");
            return;
        }
        if (!student.getCourse().equals(subject.getCourse())){
            System.out.println("Student and subject are not in the same course");
            return;
        }
            // Enroll the student in the subject
            subject.addStudent(student);
            student.addSubject(subject);
            entityManager.flush();
    }
    public void unrollStudentFromSubject(String username, long subjectCode) {
        Student student = this.find(username);
        Subject subject = entityManager.find(Subject.class, subjectCode);

        if (student == null) {
            System.out.println("Student not found");
            return;
        }
        if (subject == null) {
            System.out.println("Subject not found");
            return;
        }

        // Check if the student is enrolled in the subject
        if (!student.getSubjects().contains(subject)) {
            System.out.println("Student is not enrolled in the subject");
            return;
        }

        // Unenroll the student from the subject
        subject.removeStudent(student);
        student.removeSubject(subject);
        entityManager.merge(subject);
        entityManager.merge(student);
        entityManager.flush();
    }

    public void remove(String username) {
        Student student = entityManager.find(Student.class, username);
        entityManager.remove(student);
    }
}
