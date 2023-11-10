package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import java.util.List;

@Stateless
public class CourseBean {
    @PersistenceContext
    private EntityManager entityManager;
    public void create(long code, String name){
        var course = new Course(code, name);
        entityManager.persist(course);
    }
    public List<Course> getAll() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return entityManager.createNamedQuery("getAllCourses", Course.class).getResultList();
    }
    public Course find(long code) {
        return entityManager.find(Course.class, code);
    }
    public void update(long code, String name) {
        Course course = entityManager.find(Course.class, code);
        if (course == null) {
            System.err.println("ERROR_COURSE_NOT_FOUND: " + code);
            return;
        }
        entityManager.lock(course, LockModeType.OPTIMISTIC);
        course.setName(name);
    }

    public void remove(long code) {
        Course course = entityManager.find(Course.class, code);
        entityManager.remove(course);
    }
}
