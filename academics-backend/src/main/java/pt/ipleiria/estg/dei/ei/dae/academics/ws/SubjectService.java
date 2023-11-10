package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;
import java.util.stream.Collectors;

@Path("subjects")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SubjectService {

    @EJB
    private SubjectBean subjectBean;

    private SubjectDTO toDTO(Subject subject) {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setCode(subject.getCode());
        subjectDTO.setName(subject.getName());
        if (subject.getCourse() != null) {
            subjectDTO.setCourseCode(subject.getCourse().getCode());
            subjectDTO.setCourseName(subject.getCourse().getName());
            subjectDTO.setCourseYear(subject.getCourseYear());
            subjectDTO.setScholarYear(subject.getScholarYear());
        }
        else {
            // Handle the case where the student has no associated course
            // You can set courseCode and courseName to appropriate default values or handle it as needed.
            subjectDTO.setCourseCode(0); // or some default value
            subjectDTO.setCourseName("No Course Assigned"); // or another default message
            subjectDTO.setCourseYear(0);
            subjectDTO.setScholarYear(0);
        }
        // Populate other properties of SubjectDTO as needed
        return subjectDTO;
    }
    private TeacherDTO toDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setUsername(teacher.getUsername());
        teacherDTO.setPassword(teacher.getPassword()); // Note: You might want to avoid exposing passwords in DTOs in a real application
        teacherDTO.setName(teacher.getName());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setOffice(teacher.getOffice());

        return teacherDTO;
    }
    private StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUsername(student.getUsername());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());

        // Assuming Student entity has a Course property
        if (student.getCourse() != null) {
            studentDTO.setCourseCode(student.getCourse().getCode());
            studentDTO.setCourseName(student.getCourse().getName());
        } else {
            // Handle the case where the student has no associated course
            // You can set courseCode and courseName to appropriate default values or handle it as needed.
            studentDTO.setCourseCode(0); // or some default value
            studentDTO.setCourseName("No Course Assigned"); // or another default message
        }

        return studentDTO;
    }

    private List<SubjectDTO> toDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    // Helper method to convert a list of Teacher entities to a list of TeacherDTOs
    private List<TeacherDTO> teachersToDTOs(List<Teacher> teachers) {
        return teachers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectBean.getAllSubjects();
        return toDTOs(subjects);
    }
    @GET
    @Path("/{code}/teachers")
    public Response getSubjectTeachers(@PathParam("code") long subjectCode) {
        Subject subject = subjectBean.find(subjectCode);

        if (subject != null) {
            List<TeacherDTO> teacherDTOs = teachersToDTOs(subject.getTeachers());
            return Response.ok(teacherDTOs).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }
    @GET
    @Path("/{code}/students")
    public Response getSubjectStudents(@PathParam("code") long subjectCode) {
        Subject subject = subjectBean.find(subjectCode);

        if (subject != null) {
            List<StudentDTO> studentDTOs = studentsToDTOs(subject.getStudents());
            return Response.ok(studentDTOs).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSubject(SubjectDTO subjectDTO) {
        subjectBean.create(
                subjectDTO.getCode(),
                subjectDTO.getName(),
                subjectDTO.getCourseCode(),
                subjectDTO.getCourseYear(),
                subjectDTO.getScholarYear()
        );

        Subject newSubject = subjectBean.find(subjectDTO.getCode());
        if(newSubject == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(toDTO(newSubject)).build();
    }
    @GET
    @Path("/{code}")
    public Response getSubjectDetails(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject != null) {
            return Response.ok(toDTO(subject)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }

    @PUT
    @Path("/")
    public Response updateSubject(SubjectDTO subjectDTO) {
        Subject existingSubject = subjectBean.find(subjectDTO.getCode());
        if (existingSubject == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingSubject.setName(subjectDTO.getName());
        // Update other properties of the Subject entity from subjectDTO if necessary
        subjectBean.update(subjectDTO.getCode(), subjectDTO.getName(), subjectDTO.getCourseCode(), subjectDTO.getCourseYear(), subjectDTO.getScholarYear());
        return Response.status(Response.Status.OK).entity(toDTO(existingSubject)).build();
    }

    @DELETE
    @Path("/{code}")
    public Response deleteSubject(@PathParam("code") long code) {
        Subject existingSubject = subjectBean.find(code);
        if (existingSubject == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        subjectBean.delete(existingSubject.getCode());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
