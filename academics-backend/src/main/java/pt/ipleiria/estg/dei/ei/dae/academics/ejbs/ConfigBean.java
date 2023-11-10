package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.EJB;
@Startup
@Singleton
public class ConfigBean {
    @EJB
    private StudentBean studentBean;
    @EJB
    private CourseBean courseBean;
    @EJB
    private SubjectBean subjectBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private TeacherBean teacherBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");
        courseBean.create(1, "Engenharia Informatica");
        courseBean.create(2, "Engenharia Eletrotecnica");

        subjectBean.create(1, "P1", 1, 1, 1);
        subjectBean.create(2, "P2", 1, 2, 1);

        studentBean.create("joel", "123", "Joel", "joelsb@gmail.com",1);
        studentBean.create("manuel", "123", "Manuel", "manuel@gmail.com",2);
        studentBean.create("joao", "123", "João", "joao@gmail.com",1);

        studentBean.enrollStudentInSubject("joel", 1);
        studentBean.enrollStudentInSubject("joel", 2);
        studentBean.enrollStudentInSubject("manuel", 1);
        studentBean.enrollStudentInSubject("joao", 1);

        teacherBean.create("teacher1", "123", "Teacher1", "teacher1@gmail.com","office1");
        teacherBean.create("teacher2", "123", "Teacher2", "teacher2@gmail.com","office2");
        teacherBean.create("teacher3", "123", "Teacher3", "teacher3@gmail.com","office3");

        administratorBean.create("admin1", "123", "Admin1", "admin1@gmail.com");
        administratorBean.create("admin2", "123", "Admin2", "admin2@gmail.com");
        administratorBean.create("admin3", "123", "Admin3", "admin3@gmail.com");

    }
}