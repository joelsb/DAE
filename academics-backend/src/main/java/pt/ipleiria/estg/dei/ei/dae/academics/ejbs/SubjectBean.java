package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.util.List;

@Stateless
public class SubjectBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(long code, String name, long courseCode, int courseYear, int scholarYear) {
        Course course = entityManager.find(Course.class, courseCode);
        Subject subject = new Subject(code, name, course, courseYear, scholarYear);
        entityManager.persist(subject);
    }
    public void delete(long code) {
        Subject subject = entityManager.find(Subject.class, code);
        entityManager.remove(subject);
    }

    public List<Subject> getAllSubjects() {
        TypedQuery<Subject> query = entityManager.createNamedQuery("getAllSubjects", Subject.class);
        return query.getResultList();
    }
    public Subject find(long code) {
        return entityManager.find(Subject.class, code);
    }

    public void update(long code, String name, long courseCode, int courseYear, int scholarYear) {
        Course course = entityManager.find(Course.class, courseCode);
        Subject existingSubject = entityManager.find(Subject.class, code);
        existingSubject.setCode(code);
        existingSubject.setName(name);
        existingSubject.setCourse(course);
        existingSubject.setCourseYear(courseYear);
        existingSubject.setScholarYear(scholarYear);
        entityManager.merge(existingSubject);
    }
}
