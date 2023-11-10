package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.TeacherBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;
import java.util.stream.Collectors;

@Path("teachers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TeacherService {

    @EJB
    private TeacherBean teacherBean;
    private TeacherDTO toDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setUsername(teacher.getUsername());
        teacherDTO.setPassword(teacher.getPassword()); // Note: You might want to avoid exposing passwords in DTOs in a real application
        teacherDTO.setName(teacher.getName());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setOffice(teacher.getOffice());

        return teacherDTO;
    }
    private List<TeacherDTO> toDTOs(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
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
    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherBean.getAllTeachers();
        return toDTOs(teachers);
    }
    @GET
    @Path("/{username}/subjects")
    public Response getTeacherSubjects(@PathParam("username") String username) {
        var teacher = teacherBean.findWithSubjects(username);
        if (teacher != null) {
            var dtos = subjectsToDTOs(teacher.getSubjects());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_TEACHER")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewTeacher(TeacherDTO teacherDTO) {
        teacherBean.create(
                teacherDTO.getUsername(),
                teacherDTO.getPassword(),
                teacherDTO.getName(),
                teacherDTO.getEmail(),
                teacherDTO.getOffice()
        );

        Teacher newTeacher = teacherBean.find(teacherDTO.getUsername());
        if (newTeacher == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(toDTO(newTeacher)).build();
    }

    @GET
    @Path("/{username}")
    public Response getTeacherDetails(@PathParam("username") String username) {
        Teacher teacher = teacherBean.find(username);
        if (teacher != null) {
            return Response.ok(toDTO(teacher)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_TEACHER")
                .build();
    }

    @PUT
    @Path("/")
    public Response updateTeacher(TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherBean.find(teacherDTO.getUsername());
        if (existingTeacher == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingTeacher.setName(teacherDTO.getName());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setOffice(teacherDTO.getOffice());
        // Update other properties of the Teacher entity from teacherDTO if necessary
        teacherBean.update(existingTeacher);
        return Response.status(Response.Status.OK).entity(toDTO(existingTeacher)).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteTeacher(@PathParam("username") String username) {
        Teacher existingTeacher = teacherBean.find(username);
        if (existingTeacher == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        teacherBean.delete(existingTeacher);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
