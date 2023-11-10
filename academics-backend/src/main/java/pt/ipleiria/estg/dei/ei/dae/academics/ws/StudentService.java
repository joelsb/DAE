package pt.ipleiria.estg.dei.ei.dae.academics.ws;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//Data layer
@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class StudentService {
    @EJB
    private StudentBean studentBean;
    // Converts an entity Student to a DTO Student class
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
    // converts an entire list of entities into a list of DTOs
    private List<StudentDTO> toDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }
    public SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getCourseYear(),
                subject.getScholarYear()
        );
    }
    //Converts an entire list of entities into a list of DTOs
    public List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<StudentDTO> getAllStudents() {
        return toDTOs(studentBean.getAll());
    }
    @POST
    @Path("/")
    public Response createNewStudent (StudentDTO studentDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student newStudent = studentBean.find(studentDTO.getUsername());
        if(newStudent == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(toDTO(newStudent)).build();
    }
    @PUT
    @Path("/")
    public Response updateStudent(StudentDTO studentDTO) {
        Student student = studentBean.find(studentDTO.getUsername());
        if (student == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else{
            studentBean.update(
                    studentDTO.getUsername(),
                    studentDTO.getPassword(),
                    studentDTO.getName(),
                    studentDTO.getEmail(),
                    studentDTO.getCourseCode()
            );
            return Response.status(Response.Status.OK).entity(toDTO(student)).build();
        }
    }
    @DELETE
    @Path("/{username}")
    public Response deleteStudent(@PathParam("username") String username) {
        Student student = studentBean.find(username);
        if (student == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else{
            studentBean.remove(username);
            return Response.status(Response.Status.OK).entity(toDTO(student)).build();
        }
    }

    @GET
    @Path("{username}")
    public Response getStudentDetails(@PathParam("username") String username) {
        Student student = studentBean.find(username);
        if (student != null) {
            return Response.ok(toDTO(student)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }
    @GET
    @Path("{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        //return toDTOs(studentBean.getAll());
        var student = studentBean.findWithSubjects(username);
        if (student != null) {
            var dtos = subjectsToDTOs(student.getSubjects());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }
    @PUT
    @Path("{username}/enroll/{subjectCode}")
    public Response enrollStudentInSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        try {
            studentBean.enrollStudentInSubject(username, subjectCode);
            return Response.ok("Student enrolled successfully into " + subjectCode + ".").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error enrolling student: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("{username}/unroll/{subjectCode}")
    public Response unrollStudentFromSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        try {
            studentBean.unrollStudentFromSubject(username, subjectCode);
            return Response.ok("Student unenrolled successfully from " + subjectCode + ".").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error unenrolling student: " + e.getMessage()).build();
        }
    }

}