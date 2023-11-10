package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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
        Subject subject = entityManager.find(Subject.class, code);
        if (subject == null) {
            System.err.println("ERROR_SUBJECT_NOT_FOUND: " + code);
            return;
        }
        entityManager.lock(subject, LockModeType.OPTIMISTIC);
        subject.setName(name);

        if (subject.getCourse().getCode() != courseCode) {
            Course course = entityManager.find(Course.class, courseCode);
            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }
            subject.setCourse(course);
        }

        subject.setCourseYear(courseYear);
        subject.setScholarYear(scholarYear);
    }

}
