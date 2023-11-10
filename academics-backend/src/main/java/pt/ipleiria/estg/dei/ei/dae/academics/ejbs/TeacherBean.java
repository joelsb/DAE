package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;

@Stateless
public class TeacherBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        entityManager.persist(teacher);
    }

    public Teacher find(String username) {
        return entityManager.find(Teacher.class, username);
    }
    public Teacher findWithSubjects(String username) {
        var teacher = this.find(username);
        Hibernate.initialize(teacher.getSubjects());
        return teacher;
    }

    public List<Teacher> getAllTeachers() {
        return entityManager.createNamedQuery("getAllTeachers", Teacher.class).getResultList();
    }

    public void update(String username, String password, String name, String email, String office) {
        Teacher teacher = entityManager.find(Teacher.class, username);
        if (teacher == null) {
            System.err.println("ERROR_TEACHER_NOT_FOUND: " + username);
            return;
        }
        entityManager.lock(teacher, LockModeType.OPTIMISTIC);
        teacher.setPassword(password);
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setOffice(office);
    }

    public void delete(String username) {
        Teacher teacher = entityManager.find(Teacher.class, username);
        entityManager.remove(teacher);
    }
    public void associateSubject(String username, long subjectCode) {
        Teacher teacher = this.find(username);
        Subject subject = entityManager.find(Subject.class, subjectCode);

        if (teacher == null){
            System.out.println("Teacher not found");
            return;
        }
        if (subject == null){
            System.out.println("Subject not found");
            return;
        }
        // Associate the teacher with the subject
        teacher.addSubject(subject);
        subject.addTeacher(teacher);
    }
    public void desassociateSubject(String username, long subjectCode) {
        Teacher teacher = this.find(username);
        Subject subject = entityManager.find(Subject.class, subjectCode);

        if (teacher == null){
            System.out.println("Teacher not found");
            return;
        }
        if (subject == null){
            System.out.println("Subject not found");
            return;
        }
        // Associate the teacher with the subject
        teacher.removeSubject(subject);
        subject.removeTeacher(teacher);
    }


}
