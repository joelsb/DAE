package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
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

    public void update(Teacher existingTeacher) {

    }

    public void delete(Teacher existingTeacher) {
    }
}
